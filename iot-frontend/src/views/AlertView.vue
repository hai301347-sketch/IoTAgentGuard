<template>
  <div class="alert-page">
    <div class="toolbar">
      <el-select v-model="statusFilter" placeholder="状态筛选" style="width:150px" clearable @change="handleFilterChange">
        <el-option label="未处理" :value="0" />
        <el-option label="已处理" :value="1" />
      </el-select>
      <el-input v-model="query.keyword" placeholder="设备编码" style="width:150px;margin:0 10px" clearable @clear="loadData" @keyup.enter="loadData" />
      <el-button type="primary" @click="loadData" style="background:#16a085;border:none">搜索</el-button>
    </div>
    <el-table :data="alerts" v-loading="loading" style="width:100%;margin-top:16px" empty-text="暂无告警">
      <el-table-column prop="deviceCode" label="设备编码" width="100" />
      <el-table-column prop="alertType" label="告警类型" width="120" />
      <el-table-column prop="alertLevel" label="告警级别" width="100">
        <template #default="{row}">
          <el-tag :type="levelTag(row.alertLevel)" size="small">{{ row.alertLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="alertContent" label="告警内容" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 0 ? 'danger' : 'success'" size="small">{{ row.status === 0 ? '未处理' : '已处理' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{row}">
          <el-button v-if="row.status === 0" size="small" type="primary" @click="handleAlert(row.id)" style="background:#16a085;border:none">处理</el-button>
          <span v-else style="color:#16a085">已处理</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" layout="total, prev, pager, next"
      :total="total" :page-size="query.size" :current-page="query.page" @current-change="handlePageChange" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { alertApi } from '../api/index'

const alerts = ref([])
const total = ref(0)
const loading = ref(false)
const statusFilter = ref(null)
const query = reactive({ page: 1, size: 10, keyword: '' })

function levelTag(level) {
  if (level === 'CRITICAL') return 'danger'
  if (level === 'HIGH') return 'warning'
  return 'info'
}

function handleFilterChange() {
  query.page = 1
  loadData()
}

async function loadData() {
  loading.value = true
  try {
    const res = await alertApi.list(query)
    let list = res.data.list
    if (statusFilter.value !== null) {
      list = list.filter(a => a.status === statusFilter.value)
    }
    alerts.value = list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function handlePageChange(page) {
  query.page = page
  loadData()
}

async function handleAlert(id) {
  await alertApi.handle(id)
  ElMessage.success('告警已处理')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.toolbar { display: flex; align-items: center; }
</style>
