import { createSSEConnection } from '../utils/sse'

export function chatStream(memoryId, message, onChunk, onDone, onError) {
  return createSSEConnection(memoryId, message, onChunk, onDone, onError)
}
