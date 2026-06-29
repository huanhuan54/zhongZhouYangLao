<template>
  <div class="app-container refund-page">
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" class="query-panel">
      <el-form-item label="退款编号" prop="refundNo">
        <el-input v-model="queryParams.refundNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="申请人" prop="applicant">
        <el-input v-model="queryParams.applicant" placeholder="请输入" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="申请时间">
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
      <el-radio-group v-model="queryParams.refundStatus" size="small" @change="handleQuery">
        <el-radio-button :label="undefined">全部</el-radio-button>
        <el-radio-button :label="1">退款处理中</el-radio-button>
        <el-radio-button :label="2">退款成功</el-radio-button>
        <el-radio-button :label="3">退款失败</el-radio-button>
      </el-radio-group>
    </div>

    <el-table v-loading="loading" :data="refundList">
      <el-table-column label="序号" type="index" width="60" align="center" />
      <el-table-column label="退款编号" prop="refundNo" min-width="155" />
      <el-table-column label="订单编号" prop="orderNo" min-width="155" />
      <el-table-column label="退款金额（元）" min-width="110" align="center">
        <template slot-scope="scope">{{ money(scope.row.refundAmount) }}</template>
      </el-table-column>
      <el-table-column label="申请人" prop="applicant" width="95" />
      <el-table-column label="申请时间" prop="applyTime" min-width="155" />
      <el-table-column label="退款时间" min-width="155">
        <template slot-scope="scope">{{ scope.row.refundTime || '—' }}</template>
      </el-table-column>
      <el-table-column label="订单状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" effect="plain" :type="orderTagType(scope.row.orderStatus)">
            {{ orderStatusText(scope.row.orderStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" width="105" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" effect="plain" :type="refundTagType(scope.row.refundStatus)">
            {{ refundStatusText(scope.row.refundStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center">
        <template slot-scope="scope">
          <el-button type="text" @click="viewRefund(scope.row)">查看</el-button>
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

    <el-dialog title="查看退款记录" :visible.sync="detailOpen" width="560px" append-to-body>
      <el-descriptions v-loading="detailLoading" :column="1" class="refund-detail">
        <el-descriptions-item label="退款编号">{{ detail.refundNo }}</el-descriptions-item>
        <el-descriptions-item label="订单编号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ orderStatusText(detail.orderStatus) }}</el-descriptions-item>
        <el-descriptions-item label="退款状态">
          <span :class="refundTextClass(detail.refundStatus)">{{ refundStatusText(detail.refundStatus) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="申请人">
          {{ detail.applicant }}
          <el-tag v-if="detail.applicantType" size="mini" type="warning" effect="plain">{{ detail.applicantType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ detail.applyTime }}</el-descriptions-item>
        <el-descriptions-item label="退款原因">{{ detail.refundReason }}</el-descriptions-item>
        <el-descriptions-item label="退款渠道">{{ detail.refundChannel }}</el-descriptions-item>
        <el-descriptions-item label="退款方式">{{ detail.refundMethod }}</el-descriptions-item>
        <el-descriptions-item v-if="detail.refundTime" label="退款时间">{{ detail.refundTime }}</el-descriptions-item>
        <el-descriptions-item v-if="detail.refundTransactionNo" label="退款流水号">{{ detail.refundTransactionNo }}</el-descriptions-item>
        <el-descriptions-item v-if="detail.refundStatus === 2" label="退款金额">{{ money(detail.refundAmount) }} 元</el-descriptions-item>
        <el-descriptions-item v-if="detail.failureCode" label="失败状态码">{{ detail.failureCode }}</el-descriptions-item>
        <el-descriptions-item v-if="detail.failureReason" label="失败原因">{{ detail.failureReason }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listRefunds, getRefund } from '@/api/nursing/order'

export default {
  name: 'RefundManage',
  data() {
    return {
      loading: false,
      detailLoading: false,
      total: 0,
      refundList: [],
      dateRange: [],
      detailOpen: false,
      detail: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refundNo: '',
        orderNo: '',
        applicant: '',
        refundStatus: undefined
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
      listRefunds(params).then(res => {
        this.refundList = res.rows || []
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
      this.queryParams.refundStatus = undefined
      this.handleQuery()
    },
    viewRefund(row) {
      this.detail = {}
      this.detailOpen = true
      this.detailLoading = true
      getRefund(row.id).then(res => {
        this.detail = res.data || {}
      }).finally(() => {
        this.detailLoading = false
      })
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
    refundStatusText(status) {
      return ['', '退款处理中', '退款成功', '退款失败'][status] || '-'
    },
    refundTagType(status) {
      return ['', 'warning', 'success', 'danger'][status] || 'info'
    },
    refundTextClass(status) {
      return status === 2 ? 'success-text' : status === 3 ? 'danger-text' : 'warning-text'
    }
  }
}
</script>

<style scoped lang="scss">
.refund-page {
  .query-panel {
    padding-top: 4px;
    border-bottom: 1px solid #e8ebf0;
  }
  .status-tabs {
    padding: 14px 0;
  }
}
.refund-detail {
  padding: 6px 16px 12px;
}
.success-text { color: #16b777; }
.warning-text { color: #e6a23c; }
.danger-text { color: #f56c6c; }
::v-deep .refund-detail .el-descriptions-item__label {
  width: 110px;
  color: #738096;
}
</style>
