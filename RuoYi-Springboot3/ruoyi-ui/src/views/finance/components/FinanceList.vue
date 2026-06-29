<template>
  <div class="app-container finance-page">
    <el-form ref="queryForm" :model="query" size="small" :inline="true" class="query-panel">
      <el-form-item v-if="module === 'finance-bill'" label="账单编号">
        <el-input v-model="query.code" placeholder="请输入" clearable @keyup.enter.native="search" />
      </el-form-item>
      <el-form-item label="老人姓名">
        <el-input v-model="query.elderName" placeholder="请输入" clearable @keyup.enter.native="search" />
      </el-form-item>
      <el-form-item v-if="module === 'finance-bill'" label="老人身份证号">
        <el-input v-model="query.idCard" placeholder="请输入" clearable @keyup.enter.native="search" />
      </el-form-item>
      <el-form-item v-else label="床位号">
        <el-input v-model="query.location" placeholder="请输入" clearable @keyup.enter.native="search" />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh" @click="reset">重置</el-button>
        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      </el-form-item>
    </el-form>

    <div v-if="module === 'finance-bill'" class="bill-tools">
      <el-radio-group v-model="query.status" size="small" @change="search">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="待支付">待支付</el-radio-button>
        <el-radio-button label="已支付">已支付</el-radio-button>
        <el-radio-button label="已关闭">已关闭</el-radio-button>
      </el-radio-group>
      <el-button type="primary" icon="el-icon-plus" @click="generateOpen = true">生成月度账单</el-button>
    </div>

    <div v-if="module === 'finance-prepayment'" class="table-toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="rechargeOpen = true">充值</el-button>
    </div>

    <el-table v-loading="loading" :data="rows">
      <el-table-column type="index" label="序号" width="60" align="center" />

      <template v-if="module === 'finance-bill'">
        <el-table-column label="账单编号" prop="code" min-width="145" />
        <el-table-column label="账单类型" prop="name" width="90" />
        <el-table-column label="账单月份" width="90">
          <template slot-scope="scope">{{ scope.row._payload.month || '-' }}</template>
        </el-table-column>
        <el-table-column label="老人姓名" prop="elderName" width="86" />
        <el-table-column label="老人身份证号" prop="idCard" min-width="160" />
        <el-table-column label="账单金额（元）" width="112" align="center">
          <template slot-scope="scope">{{ money(scope.row.amount) }}</template>
        </el-table-column>
        <el-table-column label="应付金额（元）" width="112" align="center">
          <template slot-scope="scope">{{ money(scope.row._payload.payableAmount) }}</template>
        </el-table-column>
        <el-table-column label="支付截止时间" min-width="150">
          <template slot-scope="scope">{{ scope.row._payload.paymentDeadline || '-' }}</template>
        </el-table-column>
        <el-table-column label="交易状态" width="82" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" effect="plain" :type="billStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="150" />
        <el-table-column label="操作" width="142" align="center">
          <template slot-scope="scope">
            <el-button type="text" :disabled="scope.row.status !== '待支付'" @click="openPay(scope.row)">支付</el-button>
            <el-button type="text" :disabled="scope.row.status !== '待支付'" @click="openCancel(scope.row)">取消</el-button>
            <el-button type="text" @click="viewBill(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </template>

      <template v-else-if="module === 'finance-prepayment'">
        <el-table-column label="预缴款编号" prop="code" min-width="160" />
        <el-table-column label="老人姓名" prop="elderName" width="100" />
        <el-table-column label="老人身份证号" prop="idCard" min-width="170" />
        <el-table-column label="充值方式" prop="productName" width="100" />
        <el-table-column label="充值金额（元）" width="120" align="center">
          <template slot-scope="scope">{{ money(scope.row.amount) }}</template>
        </el-table-column>
        <el-table-column label="充值凭证" width="100" align="center">
          <template slot-scope="scope">
            <el-image
              v-if="scope.row._payload.voucher"
              :src="scope.row._payload.voucher"
              :preview-src-list="[scope.row._payload.voucher]"
              fit="cover"
              class="voucher-thumb"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="充值备注" min-width="190">
          <template slot-scope="scope">{{ scope.row._payload.note || '-' }}</template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" min-width="150" />
      </template>

      <template v-else-if="module === 'finance-balance'">
        <el-table-column label="老人姓名" prop="elderName" min-width="120" />
        <el-table-column label="床位号" prop="location" min-width="110" />
        <el-table-column label="预缴款余额（元）" min-width="150" align="center">
          <template slot-scope="scope">{{ money(scope.row._payload.prepaid) }}</template>
        </el-table-column>
        <el-table-column label="押金余额（元）" min-width="150" align="center">
          <template slot-scope="scope">{{ money(scope.row._payload.balance) }}</template>
        </el-table-column>
        <el-table-column label="变动时间" prop="createTime" min-width="170" />
      </template>

      <template v-else>
        <el-table-column label="老人姓名" prop="elderName" min-width="120" />
        <el-table-column label="床位号" prop="location" min-width="110" />
        <el-table-column label="欠费金额（元）" min-width="150" align="center">
          <template slot-scope="scope">{{ money(scope.row.amount) }}</template>
        </el-table-column>
        <el-table-column label="支付截止时间" min-width="180">
          <template slot-scope="scope">{{ scope.row._payload.deadline || '-' }}</template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="openArrears(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </template>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="query.pageNum"
      :limit.sync="query.pageSize"
      @pagination="load"
    />

    <el-dialog title="生成月度账单" :visible.sync="generateOpen" width="500px" append-to-body>
      <el-form ref="generateForm" :model="generateForm" :rules="generateRules" label-width="100px">
        <el-form-item label="老人姓名" prop="elderName">
          <el-select v-model="generateForm.elderName" placeholder="请选择" style="width: 100%" @change="fillElder">
            <el-option v-for="elder in elders" :key="elder.name" :label="elder.name" :value="elder.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="账单月份" prop="month">
          <el-date-picker v-model="generateForm.month" type="month" value-format="yyyy-MM" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="generateOpen = false">取消</el-button>
        <el-button type="primary" @click="submitGenerate">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="上传支付凭证" :visible.sync="payOpen" width="520px" append-to-body>
      <el-form ref="payForm" :model="payForm" :rules="payRules" label-width="100px">
        <el-form-item label="支付方式" prop="method">
          <el-select v-model="payForm.method" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in paymentMethods" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付凭证" prop="voucher">
          <image-upload v-model="payForm.voucher" :limit="1" />
        </el-form-item>
        <el-form-item label="支付备注" prop="note">
          <el-input v-model="payForm.note" type="textarea" :rows="3" maxlength="50" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="payOpen = false">取消</el-button>
        <el-button type="primary" @click="submitPay">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="填写取消原因" :visible.sync="cancelOpen" width="500px" append-to-body>
      <el-form ref="cancelForm" :model="cancelForm" :rules="cancelRules" label-width="90px">
        <el-form-item label="取消原因" prop="reason">
          <el-input v-model="cancelForm.reason" type="textarea" :rows="4" maxlength="100" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="cancelOpen = false">取消</el-button>
        <el-button type="primary" @click="submitCancel">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="上传充值凭证" :visible.sync="rechargeOpen" width="540px" append-to-body>
      <el-form ref="rechargeForm" :model="rechargeForm" :rules="rechargeRules" label-width="100px">
        <el-form-item label="老人姓名" prop="elderName">
          <el-select v-model="rechargeForm.elderName" placeholder="请选择" style="width: 100%" @change="fillRechargeElder">
            <el-option v-for="elder in elders" :key="elder.name" :label="elder.name" :value="elder.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="充值方式" prop="method">
          <el-select v-model="rechargeForm.method" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in paymentMethods" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="充值金额" prop="amount">
          <el-input-number v-model="rechargeForm.amount" :min="0.01" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="充值凭证" prop="voucher">
          <image-upload v-model="rechargeForm.voucher" :limit="1" />
        </el-form-item>
        <el-form-item label="充值备注" prop="note">
          <el-input v-model="rechargeForm.note" type="textarea" :rows="3" maxlength="50" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="rechargeOpen = false">取消</el-button>
        <el-button type="primary" @click="submitRecharge">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="查看欠费账单" :visible.sync="arrearsOpen" width="720px" append-to-body>
      <el-table :data="arrearsBills" max-height="360">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column label="账单编号" prop="billNo" min-width="160" />
        <el-table-column label="账单月份" prop="month" width="100" />
        <el-table-column label="支付截止时间" prop="deadline" min-width="170" />
        <el-table-column label="应付金额（元）" width="130" align="center">
          <template slot-scope="scope">{{ money(scope.row.amount) }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { addRecord, listRecords, updateRecord } from '@/api/nursing/operations'
import { parsePayload, serializePayload } from '@/views/operations/helpers'

export default {
  name: 'FinanceList',
  props: {
    module: { type: String, required: true }
  },
  data() {
    return {
      loading: false,
      total: 0,
      rows: [],
      query: { pageNum: 1, pageSize: 10, code: '', elderName: '', idCard: '', location: '', status: '' },
      generateOpen: false,
      payOpen: false,
      cancelOpen: false,
      rechargeOpen: false,
      arrearsOpen: false,
      arrearsBills: [],
      generateForm: { elderName: '', idCard: '', location: '', month: '' },
      payForm: { row: null, method: '', voucher: '', note: '' },
      cancelForm: { row: null, reason: '' },
      rechargeForm: { elderName: '', idCard: '', location: '', method: '', amount: 0, voucher: '', note: '' },
      elders: [
        { name: '安欣', idCard: '230203199701221029', location: '1011' },
        { name: '高启强', idCard: '230203197801221029', location: '1012' },
        { name: '陈泰', idCard: '230203196001221029', location: '1013' },
        { name: '李有田', idCard: '230203198801221029', location: '1014' }
      ],
      paymentMethods: ['微信', '支付宝', '现金', '银行卡', '其他'],
      generateRules: {
        elderName: [{ required: true, message: '请选择老人', trigger: 'change' }],
        month: [{ required: true, message: '请选择账单月份', trigger: 'change' }]
      },
      payRules: {
        method: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
        voucher: [{ required: true, message: '请上传支付凭证', trigger: 'change' }],
        note: [{ required: true, message: '请输入支付备注', trigger: 'blur' }]
      },
      cancelRules: {
        reason: [{ required: true, message: '请输入取消原因', trigger: 'blur' }]
      },
      rechargeRules: {
        elderName: [{ required: true, message: '请选择老人', trigger: 'change' }],
        method: [{ required: true, message: '请选择充值方式', trigger: 'change' }],
        amount: [{ required: true, message: '请输入充值金额', trigger: 'change' }],
        voucher: [{ required: true, message: '请上传充值凭证', trigger: 'change' }],
        note: [{ required: true, message: '请输入充值备注', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.loading = true
      listRecords(this.module, this.query).then(res => {
        this.rows = (res.rows || []).map(row => ({ ...row, _payload: parsePayload(row.payload) }))
        this.total = res.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    search() {
      this.query.pageNum = 1
      this.load()
    },
    reset() {
      this.query = { pageNum: 1, pageSize: 10, code: '', elderName: '', idCard: '', location: '', status: '' }
      this.load()
    },
    money(value) {
      return Number(value || 0).toFixed(2)
    },
    billStatusType(status) {
      if (status === '已支付') return 'success'
      if (status === '待支付') return 'warning'
      return 'danger'
    },
    fillElder(name) {
      const elder = this.elders.find(item => item.name === name)
      if (elder) Object.assign(this.generateForm, elder)
    },
    fillRechargeElder(name) {
      const elder = this.elders.find(item => item.name === name)
      if (elder) Object.assign(this.rechargeForm, elder)
    },
    submitGenerate() {
      this.$refs.generateForm.validate(valid => {
        if (!valid) return
        const month = this.generateForm.month
        const amount = 2000
        const payload = {
          month,
          payableAmount: amount,
          paymentDeadline: `${month}-28 00:00:00`,
          transactionStatus: '待支付',
          billingCycle: `${month}-01 至 ${month}-28`,
          totalDays: 28,
          details: [{ type: '添加项', feeItem: '护理费用', service: '一级护理等级', amount }],
          monthlyPayable: amount,
          arrears: amount,
          deposit: 3000,
          billAmount: amount,
          refundEstimate: 1000
        }
        addRecord('finance-bill', {
          recordType: 'monthly',
          name: '月度账单',
          code: `ZD${Date.now()}`,
          status: '待支付',
          elderName: this.generateForm.elderName,
          idCard: this.generateForm.idCard,
          location: this.generateForm.location,
          productName: '月度账单',
          amount,
          startDate: `${month}-01`,
          endDate: `${month}-28`,
          payload: serializePayload(payload),
          remark: '后台生成月度账单'
        }).then(() => {
          this.$modal.msgSuccess('月度账单生成成功')
          this.generateOpen = false
          this.generateForm = { elderName: '', idCard: '', location: '', month: '' }
          this.load()
        })
      })
    },
    openPay(row) {
      this.payForm = { row, method: '', voucher: '', note: '' }
      this.payOpen = true
    },
    submitPay() {
      this.$refs.payForm.validate(valid => {
        if (!valid) return
        const row = this.payForm.row
        const payload = {
          ...row._payload,
          transactionStatus: '已支付',
          payableAmount: 0,
          payment: {
            operator: this.$store.getters.name,
            time: this.parseTime(new Date()),
            channel: '线下支付',
            method: this.payForm.method,
            voucher: this.payForm.voucher,
            note: this.payForm.note
          }
        }
        updateRecord('finance-bill', { ...row, status: '已支付', payload: serializePayload(payload) }).then(() => {
          this.$modal.msgSuccess('支付记录已保存')
          this.payOpen = false
          this.load()
        })
      })
    },
    openCancel(row) {
      this.cancelForm = { row, reason: '' }
      this.cancelOpen = true
    },
    submitCancel() {
      this.$refs.cancelForm.validate(valid => {
        if (!valid) return
        const row = this.cancelForm.row
        const payload = {
          ...row._payload,
          transactionStatus: '已关闭',
          cancellation: {
            operator: this.$store.getters.name,
            time: this.parseTime(new Date()),
            reason: this.cancelForm.reason
          }
        }
        updateRecord('finance-bill', { ...row, status: '已关闭', payload: serializePayload(payload) }).then(() => {
          this.$modal.msgSuccess('账单已取消')
          this.cancelOpen = false
          this.load()
        })
      })
    },
    viewBill(row) {
      this.$router.push(`/finance/bill/detail/${row.id}`)
    },
    submitRecharge() {
      this.$refs.rechargeForm.validate(valid => {
        if (!valid) return
        addRecord('finance-prepayment', {
          recordType: 'recharge',
          name: '预缴款充值',
          code: `YJ${Date.now()}`,
          status: '已到账',
          elderName: this.rechargeForm.elderName,
          idCard: this.rechargeForm.idCard,
          location: this.rechargeForm.location,
          productName: this.rechargeForm.method,
          amount: this.rechargeForm.amount,
          payload: serializePayload({
            method: this.rechargeForm.method,
            voucher: this.rechargeForm.voucher,
            note: this.rechargeForm.note,
            operator: this.$store.getters.name
          }),
          remark: '预缴款充值'
        }).then(() => {
          this.$modal.msgSuccess('充值记录已保存')
          this.rechargeOpen = false
          this.rechargeForm = { elderName: '', idCard: '', location: '', method: '', amount: 0, voucher: '', note: '' }
          this.load()
        })
      })
    },
    openArrears(row) {
      this.arrearsBills = row._payload.bills || []
      this.arrearsOpen = true
    }
  }
}
</script>

<style scoped lang="scss">
.finance-page {
  .query-panel {
    padding-top: 4px;
    border-bottom: 1px solid #e8ebf0;
  }
  .bill-tools,
  .table-toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 14px 0;
  }
  .table-toolbar {
    justify-content: flex-end;
  }
  .voucher-thumb {
    width: 48px;
    height: 36px;
    border-radius: 3px;
    vertical-align: middle;
  }
  ::v-deep .el-button.is-disabled {
    color: #c0c4cc;
  }
}
</style>
