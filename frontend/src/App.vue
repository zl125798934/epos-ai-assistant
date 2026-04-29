<template>
  <div class="app-container">
    <div class="app-header">
      <h1>XXX保险保全智能助手</h1>
    </div>
    
    <div class="app-content">
      <div class="left-panel">
        <PosTable ref="posTableRef" />
        <PolicyTable ref="policyTableRef" />
      </div>
      
      <div class="right-panel">
        <div class="chat-container">
          <SessionSidebar 
            :sessions="sessions"
            :currentSessionId="currentSessionId"
            @selectSession="handleSelectSession"
            @newSession="handleNewSession"
            @deleteSession="handleDeleteSession"
          />
          <div class="chat-content">
            <ChatPanel 
              v-if="currentSessionId"
              :sessionId="currentSessionId"
              :messages="currentSessionMessages"
              :memoryId="currentSessionMemoryId"
              @updateMessages="handleUpdateMessages"
              @streamDone="refreshTables" 
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import PosTable from './components/PosTable.vue'
import PolicyTable from './components/PolicyTable.vue'
import ChatPanel from './components/ChatPanel.vue'
import SessionSidebar from './components/SessionSidebar.vue'

const posTableRef = ref(null)
const policyTableRef = ref(null)
const sessions = ref([])
const currentSessionId = ref(null)

const currentSessionMessages = computed(() => {
  const session = sessions.value.find(s => s.id === currentSessionId.value)
  return session ? session.messages : []
})

const currentSessionMemoryId = computed(() => {
  const session = sessions.value.find(s => s.id === currentSessionId.value)
  return session ? session.memoryId : null
})

const refreshTables = () => {
  if (posTableRef.value) {
    posTableRef.value.loadData()
  }
  if (policyTableRef.value) {
    policyTableRef.value.loadData()
  }
}

const handleSelectSession = (sessionId) => {
  currentSessionId.value = sessionId
}

const handleNewSession = () => {
  const newSession = {
    id: Date.now().toString(),
    title: `新对话 ${sessions.value.length + 1}`,
    createdAt: new Date(),
    messages: [
      { role: 'ai', content: '你好，我是保全智能助手小智' }
    ],
    memoryId: Math.floor(Math.random() * 1000000)
  }
  sessions.value.unshift(newSession)
  currentSessionId.value = newSession.id
}

const handleDeleteSession = (sessionId) => {
  const index = sessions.value.findIndex(s => s.id === sessionId)
  if (index === -1) return
  
  sessions.value.splice(index, 1)
  
  if (currentSessionId.value === sessionId) {
    if (sessions.value.length > 0) {
      const sorted = [...sessions.value].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
      currentSessionId.value = sorted[0].id
    } else {
      currentSessionId.value = null
    }
  }
}

const handleUpdateMessages = (newMessages) => {
  const session = sessions.value.find(s => s.id === currentSessionId.value)
  if (session) {
    session.messages = newMessages
  }
}

onMounted(() => {
  const defaultSession = {
    id: Date.now().toString(),
    title: '默认对话',
    createdAt: new Date(),
    messages: [
      { role: 'ai', content: '你好，我是保全智能助手小智' }
    ],
    memoryId: Math.floor(Math.random() * 1000000)
  }
  sessions.value.push(defaultSession)
  currentSessionId.value = defaultSession.id
})
</script>

<style scoped>
.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.app-header {
  padding: 20px;
  text-align: center;
  border-bottom: 2px solid #e4e7ed;
}

.app-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
}

.app-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.left-panel {
  width: 40%;
  padding: 24px;
  overflow-y: auto;
  border-right: 2px solid #e4e7ed;
}

.right-panel {
  width: 60%;
  padding: 24px;
}

.chat-container {
  height: 100%;
  display: flex;
  background: #ffffff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.chat-container > .chat-content {
  flex: 1;
  min-width: 0;
}
</style>
