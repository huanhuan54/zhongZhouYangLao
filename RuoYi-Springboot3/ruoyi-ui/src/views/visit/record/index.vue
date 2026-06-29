<template>
  <div class="app-container visit-record-page">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" class="filter-bar">
      <el-form-item label="来访人姓名" prop="visitorName">
        <el-input v-model="queryParams.visitorName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="来访人手机号" prop="visitorPhone">
        <el-input v-model="queryParams.visitorPhone" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="来访时间">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div class="table-toolbar">
      <el-radio-group v-model="queryParams.visitType" size="small" @change="handleQuery">
        <el-radio-button :label="undefined">全部</el-radio-button>
        <el-radio-button :label="0">参观来访</el-radio-button>
        <el-radio-button :label="1">探访来访</el-radio-button>
      </el-radio-group>
      <el-button v-hasPermi="['visit:record:add']" type="primary" icon="el-icon-plus" size="small" @click="openAdd">
        来访登记
      </el-button>
    </div>

    <el-table v-loading="loading" :data="rows">
      <el-table-column label="序号" type="index" width="70" align="center" />
      <el-table-column label="来访类型" width="110" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.visitType === 0 ? 'primary' : 'success'">
            {{ scope.row.visitType === 0 ? '参观来访' : '探访来访' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="来访人姓名" prop="visitorName" min-width="110" />
      <el-table-column label="来访人手机号" prop="visitorPhone" min-width="140" />
      <el-table-column label="老人姓名" prop="elderName" min-width="100">
        <template slot-scope="scope">{{ scope.row.elderName || '-' }}</template>
      </el-table-column>
      <el-table-column label="关系" prop="relationName" min-width="100" />
      <el-table-column label="来访时间" prop="visitTime" min-width="170" />
      <el-table-column label="创建时间" prop="createTime" min-width="170" />
      <el-table-column label="备注" prop="remark" min-width="180" show-overflow-tooltip />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="来访登记" :visible.sync="open" width="520px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="来访类型" prop="visitType">
          <el-radio-group v-model="form.visitType">
            <el-radio :label="0">参观来访</el-radio>
            <el-radio :label="1">探访来访</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="来访人姓名" prop="visitorName">
          <el-input v-model="form.visitorName" maxlength="10" />
        </el-form-item>
        <el-form-item label="来访人手机号" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" maxlength="11" />
        </el-form-item>
        <el-form-item v-if="form.visitType === 1" label="老人姓名" prop="elderName">
          <el-input v-model="form.elderName" maxlength="10" />
        </el-form-item>
        <el-form-item v-if="form.visitType === 1" label="与老人关系">
          <el-input v-model="form.relationName" placeholder="如：子女、亲属、朋友" />
        </el-form-item>
        <el-form-item label="来访时间" prop="visitTime">
          <el-date-picker
            v-model="form.visitTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择来访时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listVisitRecords, addVisitRecord } from '@/api/visit/record'

export default {
  name: 'VisitRecord',
  data() {
    return {
      loading: false,
      rows: [],
      total: 0,
      dateRange: [],
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        visitorName: undefined,
        visitorPhone: undefined,
        visitType: undefined
      },
      form: {},
      rules: {
        visitType: [{ required: true, message: '请选择来访类型', trigger: 'change' }],
        visitorName: [{ required: true, message: '请输入来访人姓名', trigger: 'blur' }],
        visitorPhone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1\d{10}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
        ],
        elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
        visitTime: [{ required: true, message: '请选择来访时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const params = this.addDateRange({ ...this.queryParams }, this.dateRange)
      listVisitRecords(params).then(res => {
        this.rows = res.rows
        this.total = res.total
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.queryParams.visitType = undefined
      this.handleQuery()
    },
    openAdd() {
      this.form = {
        visitType: 1,
        visitorName: '',
        visitorPhone: '',
        elderName: '',
        relationName: '',
        visitTime: '',
        remark: ''
      }
      this.open = true
      this.$nextTick(() => this.resetForm('form'))
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        addVisitRecord(this.form).then(() => {
          this.$modal.msgSuccess('来访登记成功')
          this.open = false
          this.getList()
        })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.visit-record-page {
  .filter-bar {
    padding: 18px 18px 2px;
    margin: -20px -20px 18px;
    border-bottom: 1px solid #ebeef5;
    background: #fff;
  }
  .table-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
  }
}
</style>
