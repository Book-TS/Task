// Xử lý sự kiện khi người dùng nhấn nút "Đăng nhập"
document
  .getElementById("login-form")
  .addEventListener("submit", function (event) {
    event.preventDefault(); // Ngăn chặn gửi biểu mẫu (form) mặc định

    // Lấy giá trị tên đăng nhập và mật khẩu từ các trường input
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // Kiểm tra thông tin đăng nhập
    if (username === "admin@gmail.com" && password === "123456") {
      window.location.href = "../Home/index.html";
    } else {
      alert("Tên đăng nhập hoặc mật khẩu không đúng!");
      // Xóa giá trị trong trường mật khẩu
      document.getElementById("password").value = "";
      // Tập trung (focus) vào trường mật khẩu
      document.getElementById("password").focus();
    }
  });
