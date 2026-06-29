<template>
  <div v-loading="loading" class="app-container bill-detail-page">
    <section class="detail-section">
      <h2>账单信息</h2>
      <el-descriptions :column="2">
        <el-descriptions-item label="账单编号">
          {{ bill.code }}
          <el-tag size="mini" effect="plain">{{ bill.name }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账单月份">{{ payload.month || '-' }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ bill.elderName }}</el-descriptions-item>
        <el-descriptions-item label="老人身份证号">{{ bill.idCard }}</el-descriptions-item>
        <el-descriptions-item label="账单金额（元）">{{ money(bill.amount) }}</el-descriptions-item>
        <el-descriptions-item label="应付金额（元）">{{ money(payload.payableAmount) }}</el-descriptions-item>
        <el-descriptions-item v-if="payload.relationOrder" label="关联订单">
          <el-link type="primary" :underline="false">{{ payload.relationOrder }}</el-link>
        </el-descriptions-item>
        <el-descriptions-item label="交易状态">
          <el-tag size="mini" effect="plain" :type="statusType(bill.status)">{{ bill.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付截止时间">{{ payload.paymentDeadline || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ bill.createBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ bill.createTime || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="payload.billingCycle" label="账单周期">{{ payload.billingCycle }}</el-descriptions-item>
        <el-descriptions-item v-if="payload.totalDays" label="共计天数">{{ payload.totalDays }} 天</el-descriptions-item>
      </el-descriptions>
    </section>

    <section class="detail-section">
      <h2>账单明细</h2>
      <el-table :data="payload.details || []" border>
        <el-table-column label="类型" prop="type" width="110" />
        <el-table-column label="费用项目" prop="feeItem" min-width="160" />
        <el-table-column label="服务内容" prop="service" min-width="180" />
        <el-table-column label="金额（元）" width="130" align="right">
          <template slot-scope="scope">{{ money(scope.row.amount) }}</template>
        </el-table-column>
      </el-table>
      <div class="summary-table">
        <div><span>每月应付</span><strong>{{ money(payload.monthlyPayable) }}</strong></div>
        <div><span>本期应付</span><strong>{{ money(payload.arrears) }}</strong></div>
        <div><span>押金</span><strong>{{ money(payload.deposit) }}</strong></div>
        <div><span>账单金额</span><strong>{{ money(payload.billAmount || bill.amount) }}</strong></div>
        <div><span>预缴款返还</span><strong>-{{ money(payload.refundEstimate) }}</strong></div>
        <div class="payable-row"><span>应付金额</span><strong>{{ money(payload.payableAmount) }}</strong></div>
      </div>
    </section>

    <section v-if="payload.payment" class="detail-section">
      <h2>支付记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="操作人">{{ payload.payment.operator || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ payload.payment.time || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付渠道">{{ payload.payment.channel || '-' }}</el-descriptions-item>
        <el-descriptions-item label="支付方式">{{ payload.payment.method || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="payload.payment.transactionNo" label="支付订单号">{{ payload.payment.transactionNo }}</el-descriptions-item>
        <el-descriptions-item label="支付金额">{{ money(bill.amount) }} 元</el-descriptions-item>
        <el-descriptions-item v-if="payload.payment.voucher" label="支付凭证">
          <el-image
            :src="payload.payment.voucher"
            :preview-src-list="[payload.payment.voucher]"
            fit="cover"
            class="payment-voucher"
          />
        </el-descriptions-item>
        <el-descriptions-item v-if="payload.payment.note" label="支付备注">{{ payload.payment.note }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="payload.refund" class="detail-section">
      <h2>退款记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="申请人">{{ payload.refund.applicant || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ payload.refund.applyTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="退款方式">{{ payload.refund.method || '-' }}</el-descriptions-item>
        <el-descriptions-item label="退款金额">{{ money(payload.refund.amount) }} 元</el-descriptions-item>
      </el-descriptions>
    </section>

    <section v-if="payload.cancellation" class="detail-section">
      <h2>取消记录</h2>
      <el-descriptions :column="1">
        <el-descriptions-item label="取消人">{{ payload.cancellation.operator || '-' }}</el-descriptions-item>
        <el-descriptions-item label="取消时间">{{ payload.cancellation.time || '-' }}</el-descriptions-item>
        <el-descriptions-item label="取消原因">{{ payload.cancellation.reason || '-' }}</el-descriptions-item>
      </el-descriptions>
    </section>

    <div class="back-row">
      <el-button type="primary" icon="el-icon-back" @click="$router.back()">返回</el-button>
    </div>
  </div>
</template>

<script>
import { getRecord } from '@/api/nursing/operations'
import { parsePayload } from '@/views/operations/helpers'

export default {
  name: 'FinanceBillDetail',
  data() {
    return {
      loading: false,
      bill: {},
      payload: {}
    }
  },
  created() {
    this.loading = true
    getRecord('finance-bill', this.$route.params.id).then(res => {
      this.bill = res.data || {}
      this.payload = parsePayload(this.bill.payload)
    }).finally(() => {
      this.loading = false
    })
  },
  methods: {
    money(value) {
      return Number(value || 0).toFixed(2)
    },
    statusType(status) {
      if (status === '已支付') return 'success'
      if (status === '待支付') return 'warning'
      return 'danger'
    }
  }
}
</script>

<style scoped lang="scss">
.bill-detail-page {
  min-height: calc(100vh - 84px);
  background: #f2f4f7;
}
.detail-section {
  margin-bottom: 14px;
  padding: 18px 24px;
  background: #fff;
  border: 1px solid #e2e6ed;
  h2 {
    margin: 0 0 20px;
    color: #202b3c;
    font-size: 17px;
    font-weight: 500;
  }
}
.summary-table {
  margin-top: 12px;
  border-top: 1px solid #ebeef5;
  div {
    display: flex;
    justify-content: space-between;
    padding: 10px 16px;
    border-bottom: 1px solid #ebeef5;
    strong { font-weight: 500; }
  }
  .payable-row {
    color: #202b3c;
    font-size: 15px;
    strong { color: #f56c6c; }
  }
}
.payment-voucher {
  width: 120px;
  height: 80px;
}
.back-row {
  padding: 8px 0 24px;
  text-align: center;
}
::v-deep .el-descriptions-item__label {
  width: 130px;
  color: #738096;
}
</style>
