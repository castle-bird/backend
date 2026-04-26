package io.project.backend.domain.auth.repository.impl;

import io.project.backend.domain.auth.repository.RefreshTokenRedisRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepositoryImpl implements RefreshTokenRedisRepository {

  private final String TOKEN_KEY_PREFIX = "refresh_token:";
  private final String INDEX_KEY_PREFIX = "refresh_token_index:";
  private final long MAX_ACTIVE_SESSION_COUNT = 3L;

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public void save(
      Long userId,
      String tokenId,
      String refreshToken,
      Instant issuedAt,
      Duration ttl
  ) {
    String tokenKeyPrefix = generateTokenKey(userId, tokenId);
    String indexKeyPrefix = generateIndexKey(userId);

    // 우선 저장 후, 오래된 것을 정리한다.
    redisTemplate.opsForValue().set(tokenKeyPrefix, refreshToken, ttl);
    redisTemplate.opsForZSet().add(indexKeyPrefix, tokenId, issuedAt.toEpochMilli());
    redisTemplate.expire(indexKeyPrefix, ttl);

    // Index에서 관리 중인 토큰 ID들 조회
    Set<String> idxTokens = findIndexTokens(indexKeyPrefix);
    if (idxTokens == null || idxTokens.isEmpty()) {
      return;
    }

    List<String> staleTokenIds = new ArrayList<>();

    // index에는 있지만 실제 토큰이 없는 경우 추가
    for (String idxToken : idxTokens) {
      String savedRefreshToken = redisTemplate.opsForValue()
          .get(generateTokenKey(userId, idxToken));

      if (savedRefreshToken == null) {
        staleTokenIds.add(idxToken);
      }
    }

    // Index에만 존재하는 토큰ID 존재할 경우 제거
    if (!staleTokenIds.isEmpty()) {
      redisTemplate.opsForZSet().remove(indexKeyPrefix, staleTokenIds.toArray());
      staleTokenIds.forEach(idxTokens::remove);
    }

    // 세션 수 초과 시 오래된 토큰부터 제거
    if (idxTokens.size() > MAX_ACTIVE_SESSION_COUNT) {
      long overCount = idxTokens.size() - MAX_ACTIVE_SESSION_COUNT;
      Set<String> oldTokenIds = redisTemplate.opsForZSet().range(indexKeyPrefix, 0, overCount - 1);

      if (oldTokenIds == null || oldTokenIds.isEmpty()) {
        return;
      }

      for (String oldTokenId : oldTokenIds) {
        redisTemplate.delete(generateTokenKey(userId, oldTokenId));
      }
      redisTemplate.opsForZSet().remove(indexKeyPrefix, oldTokenIds.toArray());
    }
  }

  @Override
  public boolean exists(Long userId, String refreshToken) {
    String indexKeyPrefix = generateIndexKey(userId);
    Set<String> idxTokens = findIndexTokens(indexKeyPrefix);

    if (idxTokens == null || idxTokens.isEmpty()) {
      return false;
    }

    List<String> staleTokenIds = new ArrayList<>();

    for (String idxToken : idxTokens) {
      String savedRefreshToken = redisTemplate.opsForValue()
          .get(generateTokenKey(userId, idxToken));

      // index에는 있지만 실제 토큰이 없는 경우 추가 → 다음 루프에서 제거
      if (savedRefreshToken == null) {
        staleTokenIds.add(idxToken);
        continue;
      }

      if (savedRefreshToken.equals(refreshToken)) {
        // 위에 index에는 있지만 실제 토큰이 없는 경우 추가한 것들 제거
        if (!staleTokenIds.isEmpty()) {
          redisTemplate.opsForZSet().remove(indexKeyPrefix, staleTokenIds.toArray());
        }

        // 토큰이 존재하는 경우 true 반환
        return true;
      }
    }

    if (!staleTokenIds.isEmpty()) {
      redisTemplate.opsForZSet().remove(indexKeyPrefix, staleTokenIds.toArray());
    }

    return false;
  }

  @Override
  public void delete(Long userId, String refreshToken) {
    String indexKeyPrefix = generateIndexKey(userId);
    Set<String> idxTokens = findIndexTokens(indexKeyPrefix);

    if (idxTokens == null || idxTokens.isEmpty()) {
      return;
    }

    List<String> removeTokenIds = new ArrayList<>();

    for (String idxToken : idxTokens) {
      String tokenKey = generateTokenKey(userId, idxToken);
      String savedRefreshToken = redisTemplate.opsForValue().get(tokenKey);

      if (savedRefreshToken == null) {
        removeTokenIds.add(idxToken);
        continue;
      }

      // index에만 존재하는 토큰 + 실제 작제하려는 토큰 모두 제거
      if (savedRefreshToken.equals(refreshToken)) {
        redisTemplate.delete(tokenKey);
        removeTokenIds.add(idxToken);
        break;
      }
    }

    if (removeTokenIds.isEmpty()) {
      return;
    }

    redisTemplate.opsForZSet().remove(indexKeyPrefix, removeTokenIds.toArray());

    // 만약 Index 비어 있는데 Redis에 남아있는 토큰이 있다면 모두 제거
    Long remainingCount = redisTemplate.opsForZSet().zCard(indexKeyPrefix);
    if (remainingCount != null && remainingCount == 0L) {
      redisTemplate.delete(indexKeyPrefix);
    }
  }

  @Override
  public void deleteAllByUserId(Long userId) {
    String indexKeyPrefix = generateIndexKey(userId);
    Set<String> idxTokens = findIndexTokens(indexKeyPrefix);

    if (idxTokens == null || idxTokens.isEmpty()) {
      redisTemplate.delete(indexKeyPrefix);
      return;
    }

    for (String idxToken : idxTokens) {
      redisTemplate.delete(generateTokenKey(userId, idxToken));
    }
    redisTemplate.delete(indexKeyPrefix);
  }

  /* ================== 공통 ================== */

  private Set<String> findIndexTokens(String indexKey) {
    return redisTemplate.opsForZSet().range(indexKey, 0, -1);
  }

  private String generateTokenKey(Long userId, String tokenId) {
    return TOKEN_KEY_PREFIX + userId + ":" + tokenId;
  }

  private String generateIndexKey(Long userId) {
    return INDEX_KEY_PREFIX + userId;
  }
}
