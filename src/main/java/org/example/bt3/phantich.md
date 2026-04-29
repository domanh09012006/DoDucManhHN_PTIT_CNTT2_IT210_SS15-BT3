# PHẦN A – PHÂN TÍCH & THIẾT KẾ

## 1. Phân tích Input và Output

### Dữ liệu đầu vào (Query Parameters)
Cấu trúc URL mẫu: `/orders?userId=1&page=0&size=10&status=PROCESSING&sortBy=createdAt&direction=desc`

| Tham số | Kiểu dữ liệu | Ý nghĩa |
| :--- | :--- | :--- |
| userId | Long | ID của người dùng đang đăng nhập |
| page | int | Số thứ tự trang (bắt đầu từ 0) |
| size | int | Số lượng bản ghi trên mỗi trang |
| status | String | Trạng thái cần lọc (Filter) |
| sortBy | String | Tên cột thực hiện sắp xếp |
| direction | String | Hướng sắp xếp (asc hoặc desc) |

### Dữ liệu đầu ra (Model trả về View)
Các thuộc tính được đưa vào đối tượng Model để hiển thị lên giao diện:
* `orders`: Danh sách dữ liệu trang hiện tại (`pageData.getContent()`).
* `currentPage`: Chỉ số trang hiện tại.
* `totalPages`: Tổng số lượng trang hiện có.
* `totalItems`: Tổng số bản ghi trên hệ thống.
* `status`, `sortBy`, `direction`: Lưu lại trạng thái filter và sort để hiển thị trên UI.

---

## 2. Giải pháp kỹ thuật (Spring Data JPA)

Để thực hiện phân trang và sắp xếp, hệ thống sử dụng các thành phần sau:

| Thành phần | Vai trò |
| :--- | :--- |
| **Page<T>** | Đối tượng bao gồm danh sách dữ liệu thực tế và các thông tin bổ trợ (metadata). |
| **Pageable** | Interface chứa cấu hình về số trang, kích thước trang và thông tin sắp xếp. |
| **Sort** | Đối tượng định nghĩa logic sắp xếp theo cột và hướng cụ thể. |

---

## 3. Luồng xử lý dữ liệu (Workflow)

1.  **Trình duyệt (Browser):** Gửi yêu cầu kèm các tham số qua URL.
2.  **Controller:** Tiếp nhận các tham số từ Request.
3.  **Service:** Kiểm tra tính hợp lệ của tham số (Validation) và xử lý các lỗi logic.
4.  **Repository:** Thực hiện truy vấn vào Cơ sở dữ liệu sử dụng đối tượng `Pageable`.
5.  **Service:** Tiếp nhận đối tượng `Page<Order>` từ Repository và trả về cho Controller.
6.  **Controller:** Đóng gói dữ liệu vào `Model`.
7.  **View:** Nhận dữ liệu từ Model để hiển thị danh sách và các nút bấm chuyển trang.

---
