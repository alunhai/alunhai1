<template>
  <div class="root">
    <el-container v-if="!user.login">
      <el-header height="200px"> </el-header>
      <el-container>
        <el-main>
          <el-form label-width="120px" size="medium">
            <el-row>
              <el-col :span="5" :offset="6">
                <el-form-item label="用户名" required>
                  <el-input v-model="user.name" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="5" :offset="6">
                <el-form-item label="密码" required>
                  <el-input v-model="user.password" show-password />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="5" :offset="8">{{ loginNotice }}</el-col>
            </el-row>
            <el-row>
              <el-col :span="3" :offset="8">
                <el-button type="primary" @click="login">登录</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-main>
      </el-container>
    </el-container>
    <el-container v-if="user.login">
      <el-aside width="200px">
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo"
          @select="selectMenu"
        >
          <el-sub-menu index="1">
            <template #title>
              <span>数据分析</span>
            </template>
            <el-menu-item-group title="">
              <el-menu-item index="1-1">性别</el-menu-item>
              <el-menu-item index="1-2">年龄</el-menu-item>
              <el-menu-item index="1-3">地区</el-menu-item>
              <el-menu-item index="1-4">职业</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <span>用户</span>
            </template>
            <el-menu-item-group title="">
              <el-menu-item index="2-1">App用户</el-menu-item>
              <el-menu-item index="2-2">管理用户</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>
        <div v-if="location.startsWith('1-')" id="chart" v-loading="chart.wait" :style="{height: chartHeight + 'px'}">
        </div>
        <div v-if="location == '2-1'">
          <div>
            <el-button @click="userAdd">添加</el-button>
            <el-button @click="userEdit">修改</el-button>
            <el-button @click="userDelete">删除</el-button>
            <el-table
              ref="user"
              highlight-current-row
              @row-click="handleSelectUser"
              :data="appUsers.items"
              style="width: 100%"
            >
              <el-table-column prop="id" label="ID" width="100" />
              <el-table-column prop="gender" label="性别" />
              <el-table-column prop="ageDesc" label="年龄" />
              <el-table-column prop="occDesc" label="职业" />
              <el-table-column prop="zipCode" label="邮编" />
            </el-table>
            <el-pagination
              v-model:currentPage="appUsers.pageNo"
              :page-size="appUsers.pageSize"
              layout="total, prev, pager, next"
              :total="appUsers.pageTotal"
              @current-change="listAppUsers"
            ></el-pagination>
          </div>
        </div>
        <div v-if="location == '2-2'">
          <div>
            <el-button @click="userAdd">添加</el-button>
            <el-button @click="userEdit">修改</el-button>
            <el-button @click="userDelete">删除</el-button>
            <el-table
              ref="user"
              highlight-current-row
              @row-click="handleSelectUser"
              :data="manageUsers.items"
              style="width: 100%"
            >
              <el-table-column prop="id" label="ID" width="600" />
              <el-table-column prop="username" label="用户名" />
              <el-table-column prop="isAdmin" label="是否超管" />
            </el-table>
            <el-pagination
              v-model:currentPage="manageUsers.pageNo"
              :page-size="manageUsers.pageSize"
              layout="total, prev, pager, next"
              :total="manageUsers.pageTotal"
              @current-change="listManageUsers"
            ></el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ElNotification } from "element-plus";

import axios from "axios";
import * as echarts from 'echarts'

axios.defaults.withCredentials = true;
const baseUrl = "http://localhost:5000/movie-analyser/";

const nameMapping = new Map()
nameMapping["1-1"] = "gender"
nameMapping["1-2"] = "age"
nameMapping["1-3"] = "occupation"
nameMapping["1-4"] = "zipcode"

export default {
  name: 'App',
  data() {
    return {
      location: "1-1",
      user: {
        login: true,
        name: null,
        password: null
      },
      chart: {
        wait: false
      },
      appUsers: {
        items: [],
        pageNo: 1,
        pageSize: 50,
        pageTotal: 0,
      },
      manageUsers: {
        items: [],
        pageNo: 1,
        pageSize: 20,
        pageTotal: 0,
      },
      itv: null,
      myChart: null,
      chartHeight: 300
    }
  },
  methods: {
    selectMenu(e) {
     this.location = e
     if(this.location == "2-1") {
       this.listAppUsers()
     } else if(this.location == "2-2") {
       this.listManageUsers()
     } else if(this.location.startsWith("1-")) {
        console.log(e)
        this.chartHeight = 300
        this.queryData(nameMapping[this.location])
     }
    },
    queryData(name) {
      const func = this.getChartData
      this.chart.wait = true
      this.itv = window.setInterval(function(){
        func(name)
      }, 10*1000)
    },
    getChartData(name) {
      const func = this.initChart
      const url = baseUrl + "/charts?type=" + name
      axios.get(url).then(res=>{
        console.log(res)
        if(!this.showErrors(res)) {
          if(res.data['obj'] == null){
            this.chart.wait = true
          } else {
            this.chart.wait = false
            window.clearInterval(this.itv)
            console.log(res.data['obj'])
            func(res.data['obj'])
          }
        }else {
          window.clearInterval(this.itv)
        }
      })
    },
    initChart(options) {
      if(this.myChart != null) {
        this.myChart.dispose()
      }
      this.myChart = echarts.init(document.getElementById('chart'));
      const numLabels = options['yAxis']['data'].length
      const numSeries = options['series'].length
      this.chartHeight = numSeries * numLabels * 20 + numLabels * 20
      console.log(numLabels, numSeries, this.chartHeight)
      const t=this
      window.setTimeout(()=>{
        t.myChart.resize({
          height: t.chartHeight
        })
        t.myChart.setOption(options)
      }, 1000);
    },
    listAppUsers() {
      this.listByPage(this.appUsers, "users")
    },
    listManageUsers() {
      this.listByPage(this.manageUsers, "manage-users")
    },
    listByPage(ctx, name) {
      const url = baseUrl + "/"+name+"/list";
      axios
        .get(url, {
          params: {
            pageNo: ctx.pageNo,
            pageSize: ctx.pageSize,
            userId: ctx.id,
          },
        })
        .then((res) => {
          if (!this.showErrors(res)) {
            const page = res.data["obj"];
            ctx.items = page["items"];
            ctx.pageNo = page["pageNo"];
            ctx.pageSize = page["pageSize"];
            ctx.pageTotal = page["total"];
            console.log(ctx);
          }
        });
    },
    showErrors(res) {
      if (res.status != 200) {
        this.showError(res.statusText);
        return true;
      } else {
        if (res.data["status"] != 0) {
          this.showError(res.data["msg"]);
          return true;
        }
      }
      return false;
    },
    showError(text) {
      ElNotification({
        title: "错误",
        message: text,
        type: "error",
      });
    },
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
