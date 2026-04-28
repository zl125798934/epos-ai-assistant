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
        <ChatPanel @streamDone="refreshTables" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import PosTable from './components/PosTable.vue'
import PolicyTable from './components/PolicyTable.vue'
import ChatPanel from './components/ChatPanel.vue'

const posTableRef = ref(null)
const policyTableRef = ref(null)

const refreshTables = () => {
  if (posTableRef.value) {
    posTableRef.value.loadData()
  }
  if (policyTableRef.value) {
    policyTableRef.value.loadData()
  }
}
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
</style>
