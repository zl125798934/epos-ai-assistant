<template>
  <div class="chat-panel">
    <div class="chat-header">
      <h3>保全智能助手</h3>
    </div>
    
    <div class="chat-messages" ref="messagesRef">
      <div 
        v-for="(msg, index) in messages" 
        :key="index"
        :class="['message-item', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="avatar">{{ msg.role === 'user' ? '我' : '小智' }}</div>
        <div class="message-content">{{ msg.content }}</div>
      </div>
    </div>
    
    <div class="chat-input">
      <el-input
        v-model="inputMessage"
        placeholder="请输入您的问题..."
        @keyup.enter="sendMessage"
        :disabled="isStreaming"
      />
      <el-button 
        type="primary" 
        @click="sendMessage"
        :loading="isStreaming"
        :disabled="!inputMessage.trim()"
      >
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { chatStream } from '../api/ai'

const emit = defineEmits(['streamDone'])

const messages = ref([
  { role: 'ai', content: '你好，我是保全智能助手小智' }
])

const inputMessage = ref('')
const isStreaming = ref(false)
const messagesRef = ref(null)
const memoryId = ref(Math.floor(Math.random() * 1000000))
let currentEventSource = null

const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isStreaming.value) return
  
  const userMessage = inputMessage.value.trim()
  messages.value.push({ role: 'user', content: userMessage })
  inputMessage.value = ''
  
  const aiMessageIndex = messages.value.length
  messages.value.push({ role: 'ai', content: '' })
  
  isStreaming.value = true
  await scrollToBottom()
  
  currentEventSource = chatStream(
    memoryId.value,
    userMessage,
    (chunk) => {
      messages.value[aiMessageIndex].content += chunk
      scrollToBottom()
    },
    () => {
      isStreaming.value = false
      currentEventSource = null
      emit('streamDone')
    },
    (error) => {
      console.error('SSE Error:', error)
      isStreaming.value = false
      currentEventSource = null
      messages.value[aiMessageIndex].content += '\n[连接出错，请重试]'
      emit('streamDone')
    }
  )
}

onMounted(() => {
  scrollToBottom()
})
</script>

<style scoped>
.chat-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
  border-radius: 20px;
  overflow: hidden;
}

.chat-header {
  padding: 20px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
}

.chat-header h3 {
  margin: 0;
  color: #303133;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.message-item {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;
}

.ai-message {
  flex-direction: row;
}

.user-message {
  flex-direction: row-reverse;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #409eff;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
}

.ai-message .avatar {
  background: #67c23a;
  margin-right: 12px;
}

.user-message .avatar {
  margin-left: 12px;
}

.message-content {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.6;
  word-break: break-word;
  white-space: pre-wrap;
}

.ai-message .message-content {
  background: #fff;
  color: #303133;
}

.user-message .message-content {
  background: #409eff;
  color: #fff;
}

.chat-input {
  padding: 16px 20px;
  background: #fff;
  border-top: 1px solid #e4e7ed;
  display: flex;
  gap: 12px;
}

.chat-input .el-input {
  flex: 1;
}
</style>
