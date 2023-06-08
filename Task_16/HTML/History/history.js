function exportToExcel() {
  // Tạo workbook mới
  var workbook = XLSX.utils.book_new();

  // Tạo worksheet mới
  var worksheet = XLSX.utils.json_to_sheet(yourDataArray);

  // Thêm worksheet vào workbook
  XLSX.utils.book_append_sheet(workbook, worksheet, "Sheet 1");

  // Xuất file Excel
  XLSX.writeFile(workbook, "data.xlsx");
}
