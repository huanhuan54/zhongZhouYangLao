<template>
  <div class="app-container module-table-page">
    <el-form ref="queryForm" :model="query" size="small" :inline="true" class="query-panel">
      <el-form-item v-for="filter in config.filters" :key="filter.prop" :label="filter.label">
        <el-input v-model="query[filter.prop]" :placeholder="filter.placeholder || '请输入'" clearable @keyup.enter.native="search" />
      </el-form-item>
      <el-form-item v-if="config.statuses && config.statuses.length" label="状态">
        <el-select v-model="query.status" placeholder="请选择" clearable>
          <el-option v-for="item in config.statuses" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh" @click="reset">重置</el-button>
        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      </el-form-item>
    </el-form>

    <div class="table-toolbar">
      <el-button v-if="config.sync" type="primary" icon="el-icon-refresh" @click="$modal.msgSuccess('数据同步成功')">同步数据</el-button>
      <el-button v-if="config.add" type="primary" icon="el-icon-plus" @click="openAdd">{{ config.add }}</el-button>
    </div>

    <div v-if="config.tabs" class="status-tabs">
      <el-radio-group v-model="query.status" size="small" @change="search">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button v-for="item in config.tabs" :key="item" :label="item">{{ item }}</el-radio-button>
      </el-radio-group>
    </div>

    <el-table v-loading="loading" :data="rows">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column
        v-for="column in config.columns"
        :key="column.prop"
        :label="column.label"
        :min-width="column.width || 100"
        :align="column.align || 'left'"
      >
        <template slot-scope="scope">
          <el-image
            v-if="column.image"
            :src="resolveImage(value(scope.row, column.prop))"
            :preview-src-list="[resolveImage(value(scope.row, column.prop))]"
            fit="cover"
            class="room-type-image"
          >
            <div slot="error" class="image-error"><i class="el-icon-picture-outline" /></div>
          </el-image>
          <el-tag v-else-if="column.tag" size="mini" effect="plain" :type="statusType(value(scope.row, column.prop))">
            {{ value(scope.row, column.prop) || '-' }}
          </el-tag>
          <span v-else>{{ formatValue(scope.row, column) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="config.actions !== false" label="操作" :width="config.actionWidth || 180" align="center">
        <template slot-scope="scope">
          <el-button v-if="config.process && scope.row.status === '待处理'" type="text" @click="openProcess(scope.row)">处理</el-button>
          <el-button v-if="config.view !== false" type="text" @click="view(scope.row)">查看</el-button>
          <el-button v-if="config.edit" type="text" @click="edit(scope.row)">修改</el-button>
          <el-button v-if="config.toggle" type="text" @click="toggle(scope.row)">{{ scope.row.status === '启用' ? '禁用' : '启用' }}</el-button>
          <el-button v-if="config.remove" type="text" class="danger-link" @click="remove(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="load"
    />

    <el-dialog title="上传体检报告" :visible.sync="healthOpen" width="520px" append-to-body>
      <el-form label-width="110px">
        <el-form-item label="老人姓名"><el-input v-model="healthForm.elderName" /></el-form-item>
        <el-form-item label="身份证号"><el-input v-model="healthForm.idCard" maxlength="18" /></el-form-item>
        <el-form-item label="体检单位"><el-input v-model="healthForm.name" /></el-form-item>
        <el-form-item label="体检报告"><file-upload v-model="healthPayload.report" :limit="1" :file-type="['pdf']" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="healthOpen = false">取消</el-button>
        <el-button type="primary" @click="submitHealth">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="填写处理结果" :visible.sync="processOpen" width="500px" append-to-body>
      <el-form label-width="90px">
        <el-form-item label="处理时间"><el-date-picker v-model="processForm.handleTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" /></el-form-item>
        <el-form-item label="处理结果"><el-input v-model="processForm.result" type="textarea" :rows="4" maxlength="100" show-word-limit /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="processOpen = false">取消</el-button>
        <el-button type="primary" @click="submitProcess">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { addRecord, deleteRecord, listRecords, updateRecord, updateRecordStatus } from '@/api/nursing/operations'
import { parsePayload, serializePayload, statusType } from '../helpers'

const configs = {
  health: {
    filters: [{ label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard' }],
    statuses: ['已入住', '未入住', '已退住'],
    columns: [
      { label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard', width: 160 },
      { label: '健康评分（分）', prop: 'payload.score', width: 110 },
      { label: '建议入住', prop: 'payload.advice' }, { label: '推荐护理等级', prop: 'productName', width: 120 },
      { label: '入住情况', prop: 'status', tag: true }, { label: '总检日期', prop: 'startDate' },
      { label: '评估时间', prop: 'createTime', width: 150 }
    ],
    add: '上传体检报告', view: true, actionWidth: 80
  },
  admission: {
    filters: [{ label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard' }],
    columns: [
      { label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard', width: 160 },
      { label: '入住床位', prop: 'location' }, { label: '护理等级', prop: 'productName', width: 120 },
      { label: '入住期限', prop: 'dateRange', width: 170 }, { label: '创建时间', prop: 'createTime', width: 150 }
    ],
    add: '发起入住申请', view: true, actionWidth: 80
  },
  checkout: {
    filters: [{ label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard' }],
    columns: [
      { label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard', width: 160 },
      { label: '退住日期', prop: 'endDate' }, { label: '退款金额', prop: 'amount', money: true },
      { label: '创建时间', prop: 'createTime', width: 150 }
    ],
    add: '发起退住申请', view: true, actionWidth: 80
  },
  'room-type': {
    filters: [{ label: '房型名称', prop: 'name' }],
    statuses: ['启用', '禁用'],
    columns: [
      { label: '房型图片', prop: 'payload.image', image: true, width: 110 }, { label: '房型名称', prop: 'name' },
      { label: '床位费用（元/月）', prop: 'amount', money: true },
      { label: '床位介绍', prop: 'payload.description', width: 200 },
      { label: '创建时间', prop: 'createTime', width: 150 }, { label: '状态', prop: 'status', tag: true }
    ],
    add: '新增房型', edit: true, toggle: true, remove: true, view: false, actionWidth: 190
  },
  contract: {
    filters: [{ label: '合同编号', prop: 'code' }, { label: '老人姓名', prop: 'elderName' }],
    statuses: ['未生效', '生效中', '已过期', '已失效'],
    columns: [
      { label: '合同编号', prop: 'code', width: 150 }, { label: '合同名称', prop: 'name', width: 130 },
      { label: '老人姓名', prop: 'elderName' }, { label: '身份证号', prop: 'idCard', width: 160 },
      { label: '合同期限', prop: 'dateRange', width: 170 }, { label: '合同状态', prop: 'status', tag: true },
      { label: '创建时间', prop: 'createTime', width: 150 }
    ],
    view: true, actionWidth: 80
  },
  customer: {
    filters: [{ label: '客户昵称', prop: 'name' }, { label: '客户手机号', prop: 'phone' }],
    columns: [
      { label: '客户昵称', prop: 'name' }, { label: '客户手机号', prop: 'phone' },
      { label: '是否签约', prop: 'payload.signed' }, { label: '服务下单次数（次）', prop: 'payload.orders' },
      { label: '绑定老人姓名', prop: 'payload.elders', width: 180 }, { label: '首次登录时间', prop: 'payload.firstLogin', width: 150 }
    ],
    view: false, actions: false
  },
  device: {
    filters: [{ label: '设备名称', prop: 'name' }],
    columns: [
      { label: '设备名称', prop: 'name', width: 130 }, { label: '备注名称', prop: 'payload.alias' },
      { label: '产品名称', prop: 'productName' }, { label: '接入位置', prop: 'location' },
      { label: '设备类型', prop: 'payload.type' }, { label: '创建时间', prop: 'createTime', width: 150 }
    ],
    add: '新增设备', sync: true, edit: true, remove: true, view: true, actionWidth: 160
  },
  'alarm-rule': {
    filters: [{ label: '报警规则名称', prop: 'name' }],
    columns: [
      { label: '报警规则名称', prop: 'name', width: 150 }, { label: '所属产品', prop: 'productName' },
      { label: '关联设备', prop: 'location' }, { label: '报警规则', prop: 'ruleText', width: 220 },
      { label: '报警生效时段', prop: 'payload.time', width: 150 }, { label: '创建时间', prop: 'createTime', width: 150 },
      { label: '状态', prop: 'status', tag: true }
    ],
    add: '新增报警规则', edit: true, toggle: true, remove: true, view: false, actionWidth: 180
  },
  'alarm-data': {
    filters: [{ label: '设备名称', prop: 'name' }],
    tabs: ['待处理', '已处理'],
    columns: [
      { label: '设备名称', prop: 'payload.device' }, { label: '备注名称', prop: 'payload.alias' },
      { label: '所属产品', prop: 'productName' }, { label: '报警数据类型', prop: 'payload.type' },
      { label: '接入位置', prop: 'location' }, { label: '数据值', prop: 'payload.value' },
      { label: '报警时间', prop: 'payload.alarmTime', width: 150 }, { label: '报警规则', prop: 'payload.rule', width: 200 },
      { label: '状态', prop: 'status', tag: true }
    ],
    process: true, view: false, actionWidth: 80
  }
}

export default {
  name: 'ModuleTable',
  props: {
    module: { type: String, required: true }
  },
  data() {
    return {
      loading: false,
      total: 0,
      rows: [],
      query: { pageNum: 1, pageSize: 10, name: '', code: '', elderName: '', idCard: '', phone: '', status: '' },
      healthOpen: false,
      healthForm: {},
      healthPayload: {},
      processOpen: false,
      processForm: {}
    }
  },
  computed: {
    config() {
      return configs[this.module]
    }
  },
  created() {
    this.load()
  },
  methods: {
    statusType,
    load() {
      this.loading = true
      listRecords(this.module, this.query).then(res => {
        this.rows = (res.rows || []).map(row => ({ ...row, _payload: parsePayload(row.payload) }))
        this.total = res.total || 0
      }).finally(() => { this.loading = false })
    },
    search() {
      this.query.pageNum = 1
      this.load()
    },
    reset() {
      this.query = { pageNum: 1, pageSize: 10, name: '', code: '', elderName: '', idCard: '', phone: '', status: '' }
      this.load()
    },
    value(row, prop) {
      if (prop.startsWith('payload.')) return row._payload[prop.slice(8)]
      if (prop === 'dateRange') return `${row.startDate || '-'} — ${row.endDate || '-'}`
      if (prop === 'ruleText') {
        const p = row._payload
        return `${p.function || ''}${p.operator || ''}${p.threshold || ''}，${p.duration || ''}`
      }
      return row[prop]
    },
    formatValue(row, column) {
      const value = this.value(row, column.prop)
      if (column.money) return Number(value || 0).toFixed(2)
      if (Array.isArray(value)) return value.join('、')
      return value === undefined || value === null || value === '' ? '-' : value
    },
    resolveImage(value) {
      if (!value) return ''
      if (/^(https?:)?\/\//.test(value) || value.startsWith('data:')) return value
      return value.startsWith('/') ? value : `/${value}`
    },
    view(row) {
      this.$router.push(`/operations/detail/${this.module}/${row.id}`)
    },
    openAdd() {
      if (this.module === 'health') {
        this.healthForm = { recordType: 'assessment', status: '未入住', productName: '一级护理等级', name: '', elderName: '', idCard: '' }
        this.healthPayload = { score: 80, advice: '建议', risk: '提示', summary: '体检报告待AI分析。' }
        this.healthOpen = true
      } else {
        this.$router.push(`/operations/form/${this.module}`)
      }
    },
    edit(row) {
      this.$router.push(`/operations/form/${this.module}/${row.id}`)
    },
    toggle(row) {
      const status = row.status === '启用' ? '禁用' : '启用'
      updateRecordStatus(this.module, row.id, status).then(() => {
        this.$modal.msgSuccess('状态修改成功')
        this.load()
      })
    },
    remove(row) {
      this.$modal.confirm(`确认删除“${row.name}”吗？`).then(() => deleteRecord(this.module, row.id)).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.load()
      })
    },
    submitHealth() {
      this.healthForm.payload = serializePayload(this.healthPayload)
      this.healthForm.code = `PG${Date.now()}`
      addRecord('health', this.healthForm).then(() => {
        this.$modal.msgSuccess('报告上传成功')
        this.healthOpen = false
        this.load()
      })
    },
    openProcess(row) {
      this.processForm = { row, handleTime: '', result: '' }
      this.processOpen = true
    },
    submitProcess() {
      const row = this.processForm.row
      const payload = { ...row._payload, handler: this.$store.getters.name, handleTime: this.processForm.handleTime, result: this.processForm.result }
      updateRecord(this.module, { ...row, status: '已处理', payload: serializePayload(payload) }).then(() => {
        this.$modal.msgSuccess('报警处理完成')
        this.processOpen = false
        this.load()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.module-table-page {
  .query-panel { padding-top: 4px; border-bottom: 1px solid #e8ebf0; }
  .table-toolbar { display: flex; justify-content: flex-end; gap: 8px; padding: 0 0 12px; }
  .status-tabs { padding: 0 0 14px; }
  .danger-link { color: #f56c6c; }
  .room-type-image {
    width: 72px;
    height: 52px;
    border-radius: 4px;
    vertical-align: middle;
  }
  .image-error {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    color: #a8abb2;
    background: #f5f7fa;
    font-size: 22px;
  }
}
</style>
