<template>
  <div class="history-page">
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="设备编码" style="width:150px" clearable @clear="loadData" />
      <el-date-picker v-model="dateRange" type="datetimerange" start-placeholder="开始时间" end-placeholder="结束时间"
        style="width:380px;margin:0 10px" value-format="YYYY-MM-DD HH:mm:ss" />
      <el-button type="primary" @click="loadData" style="background:#16a085;border:none">查询</el-button>
    </div>
    <el-table :data="dataList" v-loading="loading" style="width:100%;margin-top:16px" empty-text="暂无数据">
      <el-table-column prop="deviceCode" label="设备编码" width="120" />
      <el-table-column prop="temperature" label="温度(°C)" width="120">
        <template #default="{row}"><span :style="{color: tempColor(row.temperature)}">{{ row.temperature }}</span></template>
      </el-table-column>
      <el-table-column prop="humidity" label="湿度(%)" width="120" />
      <el-table-column prop="light" label="光照(lux)" width="120" />
      <el-table-column prop="smoke" label="烟雾" width="100">
        <template #default="{row}"><span :style="{color: row.smoke > 50 ? '#e74c3c' : '#e0e0e0'}">{{ row.smoke }}</span></template>
      </el-table-column>
      <el-table-column prop="collectTime" label="采集时间" />
    </el-table>
    <el-pagination style="margin-top:16px;justify-content:flex-end" layout="total, prev, pager, next"
      :total="total" :page-size="query.size" :current-page="query.page" @current-change="handlePageChange" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { dataApi } from '../api/index'

const dataList = ref([])
const total = ref(0)
const loading = ref(false)
const dateRange = ref([])
const query = reactive({ page: 1, size: 10, keyword: '' })

function tempColor(t) {
  if (t > 35) return '#e74c3c'
  if (t > 25) return '#e67e22'
  return '#16a085'
}

async function loadData() {
  loading.value = true
  try {
    const params = { ...query }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res = await dataApi.list(params)
    dataList.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function handlePageChange(page) {
  query.page = page
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.toolbar { display: flex; align-items: center; }
</style>
