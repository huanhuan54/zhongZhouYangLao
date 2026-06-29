<template>
  <div class="app-container order-page">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" class="query-panel">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="老人姓名" prop="elderName">
        <el-input v-model="queryParams.elderName" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="下单人" prop="orderUser">
        <el-input v-model="queryParams.orderUser" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
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

    <div class="status-tabs">
      <el-radio-group v-model="queryParams.orderStatus" size="small" @change="handleQuery">
        <el-radio-button :label="undefined">全部</el-radio-button>
        <el-radio-button :label="0">待支付</el-radio-button>
        <el-radio-button :label="1">待执行</el-radio-button>
        <el-radio-button :label="2">已执行</el-radio-button>
        <el-radio-button :label="3">已完成</el-radio-button>
        <el-radio-button :label="4">已退款</el-radio-button>
        <el-radio-button :label="5">已关闭</el-radio-button>
      </el-radio-group>
    </div>

    <el-table v-loading="loading" :data="orderList">
      <el-table-column label="序号" type="index" width="58" align="center" />
      <el-table-column label="订单编号" prop="orderNo" min-width="130" />
      <el-table-column label="老人姓名" prop="elderName" width="82" />
      <el-table-column label="床位号" prop="bedNo" width="72" align="center" />
      <el-table-column label="护理项目名称" prop="projectName" min-width="90" />
      <el-table-column label="订单金额（元）" min-width="92" align="center">
        <template slot-scope="scope">{{ money(scope.row.orderAmount) }}</template>
      </el-table-column>
      <el-table-column label="期望服务时间" prop="expectedServiceTime" min-width="150" />
      <el-table-column label="下单人" prop="orderUser" width="82" />
      <el-table-column label="创建时间" prop="createTime" min-width="150" />
      <el-table-column label="订单状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" effect="plain" :type="orderTagType(scope.row.orderStatus)">
            {{ orderStatusText(scope.row.orderStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="交易状态" width="80" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" effect="plain" :type="paymentTagType(scope.row.paymentStatus)">
            {{ paymentStatusText(scope.row.paymentStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="132" align="center">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['nursing:order:cancel']"
            type="text"
            :disabled="scope.row.orderStatus !== 0"
            @click="openCancel(scope.row)"
          >取消</el-button>
          <el-button
            v-hasPermi="['nursing:order:refund']"
            type="text"
            :disabled="!canRefund(scope.row)"
            @click="openRefund(scope.row)"
          >退款</el-button>
          <el-button type="text" @click="viewOrder(scope.row)">查看</el-button>
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

    <el-dialog title="填写取消原因" :visible.sync="cancelOpen" width="480px" append-to-body>
      <el-form ref="cancelForm" :model="actionForm" :rules="cancelRules" label-width="90px">
        <el-form-item label="取消原因" prop="reason">
          <el-select v-model="actionForm.reason" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in cancelReasons" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="cancelOpen = false">取消</el-button>
        <el-button type="primary" @click="submitCancel">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="填写退款原因" :visible.sync="refundOpen" width="500px" append-to-body>
      <el-form ref="refundForm" :model="actionForm" :rules="refundRules" label-width="90px">
        <el-form-item label="退款原因" prop="reason">
          <el-input v-model="actionForm.reason" type="textarea" :rows="4" maxlength="100" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="refundOpen = false">取消</el-button>
        <el-button type="primary" @click="submitRefund">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrders, cancelOrder, applyRefund } from '@/api/nursing/order'

export default {
  name: 'OrderManage',
  data() {
    return {
      loading: false,
      total: 0,
      orderList: [],
      dateRange: [],
      cancelOpen: false,
      refundOpen: false,
      actionForm: {},
      cancelReasons: ['家属临时取消', '老人身体不适', '服务时间冲突', '重复下单', '其他原因'],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: '',
        elderName: '',
        orderUser: '',
        orderStatus: undefined
      },
      cancelRules: {
        reason: [{ required: true, message: '请选择取消原因', trigger: 'change' }]
      },
      refundRules: {
        reason: [{ required: true, message: '请输入退款原因', trigger: 'blur' }]
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
      listOrders(params).then(res => {
        this.orderList = res.rows || []
        this.total = res.total || 0
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
      this.queryParams.orderStatus = undefined
      this.handleQuery()
    },
    money(value) {
      return Number(value || 0).toFixed(2)
    },
    orderStatusText(status) {
      return ['待支付', '待执行', '已执行', '已完成', '已退款', '已关闭'][status] || '-'
    },
    orderTagType(status) {
      return ['warning', 'warning', '', 'success', 'warning', 'danger'][status] || 'info'
    },
    paymentStatusText(status) {
      return ['待支付', '已支付', '已关闭'][status] || '-'
    },
    paymentTagType(status) {
      return ['warning', 'success', 'danger'][status] || 'info'
    },
    canRefund(row) {
      return [1, 2].includes(row.orderStatus) && row.paymentStatus === 1 && row.refundStatus === 0
    },
    viewOrder(row) {
      this.$router.push(`/order/manage/detail/${row.id}`)
    },
    openCancel(row) {
      if (row.orderStatus !== 0) return
      this.actionForm = { id: row.id, reason: '' }
      this.cancelOpen = true
    },
    openRefund(row) {
      if (!this.canRefund(row)) return
      this.actionForm = { id: row.id, reason: '' }
      this.refundOpen = true
    },
    submitCancel() {
      this.$refs.cancelForm.validate(valid => {
        if (!valid) return
        cancelOrder(this.actionForm.id, this.actionForm.reason).then(() => {
          this.$modal.msgSuccess('订单已取消')
          this.cancelOpen = false
          this.getList()
        })
      })
    },
    submitRefund() {
      this.$refs.refundForm.validate(valid => {
        if (!valid) return
        applyRefund(this.actionForm.id, this.actionForm.reason).then(() => {
          this.$modal.msgSuccess('退款申请已提交')
          this.refundOpen = false
          this.getList()
        })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.order-page {
  .query-panel {
    padding-top: 4px;
    border-bottom: 1px solid #e8ebf0;
  }
  .status-tabs {
    padding: 14px 0;
  }
  ::v-deep .el-button.is-disabled {
    color: #c0c4cc;
  }
}
</style>
