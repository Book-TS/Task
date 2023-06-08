// Import the functions you need from the SDKs you need
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.15.0/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.15.0/firebase-analytics.js";
import {
  getAuth,
  signInWithEmailAndPassword,
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

// Xử lý sự kiện khi người dùng nhấn nút "Đăng nhập"
document
  .getElementById("login-form")
  .addEventListener("submit", function (event) {
    event.preventDefault(); // Ngăn chặn gửi biểu mẫu (form) mặc định

    if(email.value == "" || password.value == "") {
      alert("Vui lòng nhập đầy đủ thông tin");
    }

    var obj = {
      email: email.value,
      password: password.value,
    };
  
    console.log(obj);
  
    signInWithEmailAndPassword(auth, obj.email, obj.password)
      .then(function (success) {
        var aaaa = success.user.uid;
        localStorage.setItem("uid", aaaa);
        console.log(aaaa);
        window.location.replace('../Home/index.html');
      })
      .catch(function (err) {
        alert("Tên tài khoản hoặc mật khẩu không chính xác");
      });
  });