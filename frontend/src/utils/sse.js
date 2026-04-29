export function createSSEConnection(memoryId, message, onChunk, onDone, onError) {
  const encodedMessage = encodeURIComponent(message)
  const url = `/api/ai/chat?memoryId=${memoryId}&message=${encodedMessage}`
  
  const eventSource = new EventSource(url)
  let isDone = false
  
  eventSource.onmessage = (event) => {
    if (event.data === '[DONE]') {
      isDone = true
      eventSource.close()
      if (onDone) {
        onDone()
      }
      return
    }
    if (event.data) {
      onChunk(event.data)
    }
  }
  
  eventSource.onerror = (error) => {
    eventSource.close()
    if (isDone) {
      return
    }
    if (onError) {
      onError(error)
    }
  }
  
  return eventSource
}
