export function createSSEConnection(memoryId, message, onChunk, onDone, onError) {
  const encodedMessage = encodeURIComponent(message)
  const url = `/api/ai/chat?memoryId=${memoryId}&message=${encodedMessage}`
  
  const eventSource = new EventSource(url)
  
  eventSource.onmessage = (event) => {
    if (event.data && event.data !== '[DONE]') {
      onChunk(event.data)
    }
  }
  
  eventSource.onerror = (error) => {
    if (onError) {
      onError(error)
    }
    eventSource.close()
  }
  
  eventSource.addEventListener('done', () => {
    eventSource.close()
    if (onDone) {
      onDone()
    }
  })
  
  return eventSource
}
