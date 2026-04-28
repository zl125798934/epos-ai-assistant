<template>
  <div class="table-section">
    <div class="table-header">
      <h4>保单列表</h4>
      <el-button size="small" @click="loadData" :loading="loading">刷新</el-button>
    </div>
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="policyNo" label="保单号" width="120" />
      <el-table-column prop="applicant" label="投保人" width="100" />
      <el-table-column prop="purchaseDate" label="投保日期" width="120" />
      <el-table-column label="保单状态" width="120">
        <template #default="{ row }">
          <span>{{ getStatusIcon(row.policyStatus) }} {{ row.policyStatus }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="changeReason" label="变更原因" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, defineExpose } from 'vue'
import { getPolicyList } from '../api/operate'

const tableData = ref([])
const loading = ref(false)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPolicyList()
    tableData.value = res.data
  } catch (error) {
    console.error('获取保单列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusIcon = (status) => {
  const statusMap = {
    '生效中': '✅',
    '已失效': '❌',
    '已退保': '️',
    '已取消': '️'
  }
  return statusMap[status] || ''
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
