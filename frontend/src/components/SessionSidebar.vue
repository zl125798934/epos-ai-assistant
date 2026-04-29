<template>
  <div class="session-sidebar">
    <div class="session-header">
      <el-button 
        type="primary" 
        @click="createNewSession"
        class="new-session-btn"
      >
        <el-icon><Plus /></el-icon>
        开启新对话
      </el-button>
    </div>
    
    <div class="session-list">
      <div 
        v-for="session in sortedSessions" 
        :key="session.id"
        :class="['session-item', { active: currentSessionId === session.id }]"
        @click="selectSession(session.id)"
      >
        <div class="session-title">{{ session.title }}</div>
        <el-icon 
          class="delete-icon"
          @click.stop="deleteSession(session.id)"
        >
          <Delete />
        </el-icon>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Plus, Delete } from '@element-plus/icons-vue'

const props = defineProps({
  sessions: {
    type: Array,
    default: () => []
  },
  currentSessionId: {
    type: String,
    default: null
  }
})

const emit = defineEmits(['selectSession', 'newSession', 'deleteSession'])

const sortedSessions = computed(() => {
  return [...props.sessions].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
})

const createNewSession = () => {
  emit('newSession')
}

const selectSession = (sessionId) => {
  emit('selectSession', sessionId)
}

const deleteSession = (sessionId) => {
  emit('deleteSession', sessionId)
}
</script>

<style scoped>
.session-sidebar {
  width: 240px;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #ffffff;
  border-right: 1px solid #e4e7ed;
}

.session-header {
  padding: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.new-session-btn {
  width: 100%;
}

.session-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.session-item {
  padding: 12px 16px;
  margin-bottom: 4px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: all 0.2s;
}

.session-item:hover {
  background: #f5f7fa;
}

.session-item.active {
  background: #ecf5ff;
  color: #409eff;
}

.session-item.active .delete-icon {
  color: #409eff;
}

.session-item.active .delete-icon:hover {
  color: #f56c6c;
}

.session-title {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
}

.delete-icon {
  margin-left: 8px;
  cursor: pointer;
  color: #909399;
  font-size: 16px;
  opacity: 0;
  transition: opacity 0.2s;
}

.session-item:hover .delete-icon {
  opacity: 1;
}

.delete-icon:hover {
  color: #f56c6c;
}
</style>
