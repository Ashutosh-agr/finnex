/**
 * Simple in-memory rate limiter for API routes
 * In production, consider using Redis for distributed rate limiting
 */

interface RateLimitEntry {
  count: number
  resetTime: number
}

const rateLimitStore = new Map<string, RateLimitEntry>()

// Clean up old entries every 5 minutes
setInterval(() => {
  const now = Date.now()
  for (const [key, entry] of rateLimitStore.entries()) {
    if (now > entry.resetTime) {
      rateLimitStore.delete(key)
    }
  }
}, 5 * 60 * 1000)

export interface RateLimitOptions {
  limit: number // Max requests
  window: number // Time window in milliseconds
}

export interface RateLimitResult {
  success: boolean
  limit: number
  remaining: number
  reset: number
}

/**
 * Check if a request should be rate limited
 * @param identifier - Unique identifier (e.g., IP address or user ID)
 * @param options - Rate limit configuration
 * @returns Rate limit result
 */
export function rateLimit(
  identifier: string,
  options: RateLimitOptions = {limit: 60, window: 60000}
): RateLimitResult {
  const now = Date.now()
  const entry = rateLimitStore.get(identifier)

  // First request or reset time passed
  if (!entry || now > entry.resetTime) {
    const resetTime = now + options.window
    rateLimitStore.set(identifier, {count: 1, resetTime})
    return {
      success: true,
      limit: options.limit,
      remaining: options.limit - 1,
      reset: resetTime,
    }
  }

  // Within the time window
  if (entry.count < options.limit) {
    entry.count++
    return {
      success: true,
      limit: options.limit,
      remaining: options.limit - entry.count,
      reset: entry.resetTime,
    }
  }

  // Rate limit exceeded
  return {
    success: false,
    limit: options.limit,
    remaining: 0,
    reset: entry.resetTime,
  }
}

/**
 * Get client identifier from request
 * Uses IP address or user ID from JWT
 */
export function getClientIdentifier(request: Request, userId?: string): string {
  // Prefer user ID if available (more accurate for authenticated users)
  if (userId) {
    return `user:${userId}`
  }

  // Fall back to IP address
  const forwarded = request.headers.get("x-forwarded-for")
  const ip = forwarded ? forwarded.split(",")[0] : "unknown"
  return `ip:${ip}`
}
