<template>
  <div class="table-section">
    <div class="table-header">
      <h4>保全受理列表</h4>
      <el-button size="small" @click="loadData" :loading="loading">刷新</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="acceptNo" label="受理单号" width="100" />
      <el-table-column prop="applicant" label="申请人" width="100" />
      <el-table-column prop="policyNo" label="保单号" width="120" />
      <el-table-column prop="posType" label="保全类型" width="120" />
      <el-table-column label="保全状态" width="120">
        <template #default="{ row }">
          <span>{{ getStatusIcon(row.posStatus) }} {{ row.posStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核结果" width="120">
        <template #default="{ row }">
          <span>{{ getAuditIcon(row.auditResult) }} {{ row.auditResult || '-' }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, defineExpose } from 'vue'
import { getPosList } from '../api/operate'

const tableData = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPosList()
    tableData.value = res.data
  } catch (error) {
    console.error('获取保全受理列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusIcon = (status) => {
  const statusMap = {
    '已受理': '️',
    '处理中': '️',
    '处理成功': '✅',
    '处理失败': '❌',
    '已取消': '️'
  }
  return statusMap[status] || ''
}

const getAuditIcon = (result) => {
  if (!result) return ''
  const auditMap = {
    '通过': '✅',
    '拒绝': ''
  }
  return auditMap[result] || ''
}

defineExpose({ loadData })

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.table-section {
  margin-bottom: 24px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.table-header h4 {
  margin: 0;
  color: #303133;
}
</style>
