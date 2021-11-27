<template>
  <div class="root">
    <el-container v-if="!user.login">
      <el-header height="200px"> 
      </el-header>
      <el-container>
        <el-main>
          <el-form label-width="120px" size="medium">
            <el-row>
              <el-col :span="5" :offset="6">
                <el-form-item label="用户名" required>
                  <el-input v-model="user.username" />
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
            <el-button @click="editUserAct = 'add';editUserActOn=true">添加</el-button>
            <el-button @click="editUserAct = 'edit';editUserActOn=true">修改</el-button>
            <el-table
              ref="user"
              highlight-current-row
              @current-change="handleSelectUser"
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
            <el-dialog v-model="editUserActOn" title="Shipping address">
              <el-form label-position="left" label-width="120px">
                <el-form-item label="ID">
                  <el-input v-model="editUser.id" disabled/>
                </el-form-item>
                <el-form-item label="性别">
                <el-col>
                  <el-select v-model="editUser.gender" placeholder="Select">
                    <el-option label="Male" value="M" />
                    <el-option label="Female" value="F" />
                  </el-select>
                </el-col>
                </el-form-item>
                <el-form-item label="年龄">
                <el-col>
                  <el-select v-model="editUser.age" placeholder="Select">
                    <el-option label="under 18" value="1" />
                    <el-option label="18-24" value="18" />
                    <el-option label="25-34" value="25" />
                    <el-option label="35-44" value="35" />
                    <el-option label="45-49" value="45" />
                    <el-option label="50-55" value="50" />
                    <el-option label="56+" value="56" />
                  </el-select>
                </el-col>
                </el-form-item>
                <el-form-item label="职业">
                <el-col>
                  <el-select v-model="editUser.occupation" placeholder="Select">
                    <el-option label="other or not specified" value="0" />
                    <el-option label="academic/educator" value="1" />
                    <el-option label="artist" value="2" />
                    <el-option label="clerical/admin" value="3" />
                    <el-option label="college/grad student" value="4" />
                    <el-option label="customer service" value="5" />
                    <el-option label="doctor/health care" value="6" />
                    <el-option label="executive/managerial" value="7" />
                    <el-option label="farmer" value="8" />
                    <el-option label="homemaker" value="9" />
                    <el-option label="K-12 student" value="10" />
                    <el-option label="lawyer" value="11" />
                    <el-option label="programmer" value="12" />
                    <el-option label="retired" value="13" />
                    <el-option label="sales/marketing" value="14" />
                    <el-option label="scientist" value="15" />
                    <el-option label="self-employed" value="16" />
                    <el-option label="technician/engineer" value="17" />
                    <el-option label="tradesman/craftsman" value="18" />
                    <el-option label="unemployed" value="19" />
                    <el-option label="writer" value="20" />
                  </el-select>
                </el-col>
                </el-form-item>
                <el-form-item label="zipCode">
                  <el-input v-model="editUser.zipCode" />
                </el-form-item>
              </el-form>
              <el-button-group>
                  <el-button type="primary" @click="userAct">确定</el-button> &nbsp;&nbsp;
                  <el-button type="primary" @click="editUserActOn=false;editUserAct=null">取消</el-button>
              </el-button-group>
            </el-dialog>
          </div>
        </div>
        <div v-if="location == '2-2'">
          <div>
            <el-button @click="editManageUserAct='add';editManageUserActOn=true">添加</el-button>
            <el-button @click="editManageUserAct='edit';editManageUserActOn=true">修改</el-button>
            <el-button @click="editManageUserAct='delete';manageUserAct()">删除</el-button>
            <el-table
              ref="user"
              highlight-current-row
              @current-change="handleSelectManageUser"
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
            <el-dialog v-model="editManageUserActOn" title="管理用户修改">
              <el-form label-position="left" label-width="120px">
                <el-form-item label="ID">
                  <el-input v-model="editManageUser.id" disabled />
                </el-form-item>
                <el-form-item label="用户名">
                  <el-input v-model="editManageUser.username" :disabled="!user.isAdmin" />
                </el-form-item>
                <el-form-item label="密码">
                  <el-input v-model="editManageUser.password" type="password" :show-password="user.isAdmin" :disabled="!user.isAdmin" />
                </el-form-item>
                <el-form-item label="职业">
                <el-col>
                  <el-select v-model="editUser.occupation" placeholder="Select" :disabled="!user.isAdmin">
                    <el-option value="true" />
                    <el-option value="false" />
                  </el-select>
                </el-col>
                </el-form-item>
              </el-form>
              <el-button-group>
                  <el-button type="primary" @click="manageUserAct">确定</el-button> &nbsp;&nbsp;
                  <el-button type="primary" @click="editManageUserActOn=false;editManageUserAct=null">取消</el-button>
              </el-button-group>
            </el-dialog>
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
        login: false,
        username: "",
        password: "",
        isAdmin: false
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
      chartHeight: 300,
      editUserActOn: false,
      editUserAct: null,
      editUser: {
        id: 0,
        gender: "F",
        age: "",
        occupation: "",
        zipCode: ""
      },
      editManageUserActOn: false,
      editManageUserAct: null,
      editManageUser: {
        id: 0,
        username: "",
        password: "",
        isAdmin: ""
      }

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
    handleSelectUser(e) {
      if(e != null){
        this.editUser.id = e.id
        this.editUser.gender = e.gender
        this.editUser.age = e.age
        this.editUser.occupation = e.occupation
        this.editUser.zipCode = e.zipCode
        console.log("select ", this.editUser)
      }
    },
    userAct() {
      if(this.editUserAct == "add") {
        const param = {
          "gender": this.editUser.gender,
          "age": this.editUser.age,
          "occupation": this.editUser.occupation,
          "zipCode": this.editUser.zipCode
        }
        const url = baseUrl + "/users"
        axios.post(url, param).then(res => {
          if(!this.showErrors(res)) {
            this.editUserAct = null
            this.editUserActOn = false
          }
          this.listAppUsers()
        })
      } else if(this.editUserAct == 'edit') {
        const param = {
          "id": this.editUser.id,
          "gender": this.editUser.gender,
          "age": this.editUser.age,
          "occupation": this.editUser.occupation,
          "zipCode": this.editUser.zipCode
        }
        const url = baseUrl + "/users"
        axios.put(url, param).then(res => {
          if(!this.showErrors(res)) {
            this.editUserAct = null
            this.editUserActOn = false
          }
          this.listAppUsers()
        })
      }
    },
    handleSelectManageUser(e) {
      if(e != null){
        this.editManageUser.id = e.id
        this.editManageUser.username = e.username
        this.editManageUser.password = e.password
        this.editManageUser.isAdmin = e.isAdmin
      }
    },
    manageUserAct() {
      if(this.editManageUserAct == "add") {
        const param = {
          "username": this.editManageUser.username,
          "password": this.editManageUser.password,
          "isAdmin": this.editManageUser.isAdmin,
        }
        const url = baseUrl + "/manage-users"
        axios.post(url, param).then(res => {
          if(!this.showErrors(res)) {
            this.editManageUserActOn = false
            this.editManageUserAct = null
          }
          this.listManageUsers()
        })
      } else if(this.editManageUserAct == "edit") {
        const param = {
          "id": this.editManageUser.id,
          "username": this.editManageUser.username,
          "password": this.editManageUser.password,
          "isAdmin": this.editManageUser.isAdmin,
        }
        const url = baseUrl + "/manage-users"
        axios.put(url, param).then(res => {
          if(!this.showErrors(res)) {
            this.editManageUserActOn = false
            this.editManageUserAct = null
          }
          this.listManageUsers()
        })
      } else if(this.editManageUserAct == "delete") {
        const url = baseUrl + "/manage-users/" + this.editManageUser.id
        axios.delete(url).then(res => {
          if(!this.showErrors(res)) {
            this.editManageUserActOn = false
            this.editManageUserAct = null
          }
          this.listManageUsers()
        })
      }
    },
    login() {
      const url = baseUrl + "/manage-users/login"
      const param = {
        "username" : this.user.username,
        "password" : this.user.password
      }
      axios.post(url, param).then(res => {
        if(!this.showErrors(res)) {
          this.user.login = true
          this.user.username = res.data['obj']['username']
          this.user.password = res.data['obj']['password']
          this.user.isAdmin = res.data['obj']['isAdmin']
        }
      })
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
    }
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
