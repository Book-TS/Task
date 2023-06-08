// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.15.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.15.0/firebase-analytics.js";
import {
  getAuth,
  createUserWithEmailAndPassword,
} from "https://www.gstatic.com/firebasejs/9.15.0/firebase-auth.js";

const firebaseConfig = {
    apiKey: "AIzaSyCpPpRkjFtYINVc2QJSimkbCtGIe5zQUrM",
    authDomain: "dieu-khien-nhiet-do-dixell.firebaseapp.com",
    databaseURL: "https://dieu-khien-nhiet-do-dixell-default-rtdb.firebaseio.com",
    projectId: "dieu-khien-nhiet-do-dixell",
    storageBucket: "dieu-khien-nhiet-do-dixell.appspot.com",
    messagingSenderId: "961902819526",
    appId: "1:961902819526:web:cf034f57e9acac8bfa7fc7",
    measurementId: "G-RWC55H8W3Z"
  };

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const auth = getAuth();

var email = document.getElementById("email");
var password = document.getElementById("password");
var password_1 = document.getElementById("password_1");
var signin = document.getElementById("signin");

signin.addEventListener("click", (e) => {
  if(email.value == "" || password.value == "" || password_1.value == "") {
    alert("Vui lòng nhập đầy đủ thông tin");
  }
  if(password.value != password_1.value) {
    alert("Nhập lại mật khẩu không đúng");
  }

  e.preventDefault();

  var obj = {
    email: email.value,
    password: password.value,
  };

  createUserWithEmailAndPassword(auth, obj.email, obj.password)
  .then(function(success){
    window.location.replace('../Login/login.html');
    
    alert("Đăng ký thành công")
  })
  .catch(function(err){
    alert("Đăng ký không thành công")
  });
  console.log(obj);
});