<template>
  <div class="app-container appointment-page">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" class="filter-bar">
      <el-form-item label="预约人姓名" prop="visitorName">
        <el-input v-model="queryParams.visitorName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="预约人手机号" prop="visitorPhone">
        <el-input v-model="queryParams.visitorPhone" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="预约状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="预约时间">
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
        <el-radio-button :label="0">参观预约</el-radio-button>
        <el-radio-button :label="1">探访预约</el-radio-button>
      </el-radio-group>
      <el-button v-hasPermi="['visit:appointment:add']" type="primary" icon="el-icon-plus" size="small" @click="openAdd">
        预约登记
      </el-button>
    </div>

    <el-table v-loading="loading" :data="rows">
      <el-table-column label="序号" type="index" width="70" align="center" />
      <el-table-column label="预约类型" width="110" align="center">
        <template slot-scope="scope">
          <el-tag size="small" :type="scope.row.visitType === 0 ? 'primary' : 'success'">
            {{ scope.row.visitType === 0 ? '参观预约' : '探访预约' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预约人姓名" prop="visitorName" min-width="110" />
      <el-table-column label="预约人手机号" prop="visitorPhone" min-width="140" />
      <el-table-column label="老人姓名" prop="elderName" min-width="100">
        <template slot-scope="scope">{{ scope.row.elderName || '-' }}</template>
      </el-table-column>
      <el-table-column label="预约时间" prop="appointmentTime" min-width="170" />
      <el-table-column label="创建时间" prop="createTime" min-width="170" />
      <el-table-column label="预约状态" width="105" align="center">
        <template slot-scope="scope">
          <el-tag size="small" effect="plain" :type="statusMeta(scope.row.status).type">
            {{ statusMeta(scope.row.status).label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['visit:appointment:arrive']"
            type="text"
            size="mini"
            @click="openArrival(scope.row)"
          >到院</el-button>
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['visit:appointment:edit']"
            type="text"
            size="mini"
            class="cancel-action"
            @click="handleCancel(scope.row)"
          >取消</el-button>
          <span v-if="scope.row.status !== 0" class="muted">-</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="预约登记" :visible.sync="addOpen" width="520px" append-to-body>
      <el-form ref="appointmentForm" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="预约类型" prop="visitType">
          <el-radio-group v-model="form.visitType">
            <el-radio :label="0">参观预约</el-radio>
            <el-radio :label="1">探访预约</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="预约人姓名" prop="visitorName">
          <el-input v-model="form.visitorName" maxlength="10" show-word-limit />
        </el-form-item>
        <el-form-item label="预约人手机号" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" maxlength="11" />
        </el-form-item>
        <el-form-item v-if="form.visitType === 1" label="老人姓名" prop="elderName">
          <el-input v-model="form.elderName" maxlength="10" />
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker
            v-model="form.appointmentTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择预约时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOpen = false">取消</el-button>
        <el-button type="primary" @click="submitAppointment">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="确认到院时间" :visible.sync="arrivalOpen" width="480px" append-to-body>
      <el-form label-width="100px">
        <el-form-item label="来访时间" required>
          <el-date-picker
            v-model="arrivalTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择来访时间"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="arrivalOpen = false">取消</el-button>
        <el-button type="primary" @click="confirmArrival">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAppointments, addAppointment, arriveAppointment, cancelAppointment } from '@/api/visit/appointment'

function nowText() {
  const d = new Date()
  const pad = value => String(value).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

export default {
  name: 'VisitAppointment',
  data() {
    const phoneValidator = (rule, value, callback) => {
      /^1\d{10}$/.test(value || '') ? callback() : callback(new Error('请输入正确的11位手机号'))
    }
    return {
      loading: false,
      rows: [],
      total: 0,
      dateRange: [],
      addOpen: false,
      arrivalOpen: false,
      arrivalId: undefined,
      arrivalTime: '',
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        visitorName: undefined,
        visitorPhone: undefined,
        visitType: undefined,
        status: undefined
      },
      statusOptions: [
        { label: '待上门', value: 0 },
        { label: '已完成', value: 1 },
        { label: '已取消', value: 2 },
        { label: '已过期', value: 3 }
      ],
      form: {},
      rules: {
        visitType: [{ required: true, message: '请选择预约类型', trigger: 'change' }],
        visitorName: [{ required: true, message: '请输入预约人姓名', trigger: 'blur' }],
        visitorPhone: [{ required: true, validator: phoneValidator, trigger: 'blur' }],
        elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
        appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    statusMeta(status) {
      return [
        { label: '待上门', type: 'warning' },
        { label: '已完成', type: 'success' },
        { label: '已取消', type: 'danger' },
        { label: '已过期', type: 'info' }
      ][status] || { label: '未知', type: 'info' }
    },
    getList() {
      this.loading = true
      const params = this.addDateRange({ ...this.queryParams }, this.dateRange)
      listAppointments(params).then(res => {
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
        appointmentTime: '',
        remark: ''
      }
      this.addOpen = true
      this.$nextTick(() => this.resetForm('appointmentForm'))
    },
    submitAppointment() {
      this.$refs.appointmentForm.validate(valid => {
        if (!valid) return
        addAppointment(this.form).then(() => {
          this.$modal.msgSuccess('预约登记成功')
          this.addOpen = false
          this.getList()
        })
      })
    },
    openArrival(row) {
      this.arrivalId = row.id
      this.arrivalTime = nowText()
      this.arrivalOpen = true
    },
    confirmArrival() {
      if (!this.arrivalTime) {
        this.$modal.msgWarning('请选择来访时间')
        return
      }
      arriveAppointment(this.arrivalId, this.arrivalTime).then(() => {
        this.$modal.msgSuccess('到院确认成功，已生成来访记录')
        this.arrivalOpen = false
        this.getList()
      })
    },
    handleCancel(row) {
      this.$modal.confirm(`确认取消“${row.visitorName}”的预约吗？`).then(() => {
        return cancelAppointment(row.id)
      }).then(() => {
        this.$modal.msgSuccess('预约已取消')
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>

<style scoped lang="scss">
.appointment-page {
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
  .cancel-action {
    color: #f56c6c;
  }
  .muted {
    color: #c0c4cc;
  }
}
</style>
