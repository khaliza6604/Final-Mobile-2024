# H071221101 - KHALIZATUL JANNAH

ReadMe! adalah sebuah aplikasi literatur inovatif yang menyediakan berbagai judul buku terbaru yang menarik untuk dibaca. Aplikasi ini dirancang untuk memudahkan pengguna dalam menemukan dan menyimpan daftar buku yang ingin dibaca. Dengan ReadMe!, pengguna dapat menjelajahi koleksi buku yang terus diperbarui, membuat daftar bacaan pribadi, dan menemukan rekomendasi buku sesuai minat mereka. Aplikasi ini bertujuan untuk menjadi teman setia para pecinta buku dalam menjelajahi dunia literatur dengan lebih mudah dan menyenangkan.

## Fiture

Berikut adalah deskripsi fitur untuk aplikasi ReadMe! yang telah diperbaiki dan dikembangkan:

### Fitur

**1. Login & Register:**
Pengguna dapat membuat akun baru atau masuk menggunakan akun yang sudah ada. Proses registrasi dan login dilakukan dengan aman menggunakan database SQLite yang menyimpan data pengguna dengan terenkripsi.

**2. Home:**
- **Buku dan Judulnya:** Halaman utama menampilkan daftar buku terbaru beserta judulnya. Pengguna dapat dengan mudah melihat koleksi buku yang tersedia.
- **Pencarian:** Fitur pencarian memungkinkan pengguna untuk menemukan buku dengan mengetikkan judul lengkap buku yang diinginkan.
- **Best Book dari Rank 1-5:** Menampilkan buku-buku terbaik berdasarkan peringkat dari 1 hingga 5, memberikan referensi buku populer kepada pengguna.
- **Rekomendasi Buku:** Menyajikan rekomendasi buku berdasarkan minat dan riwayat bacaan pengguna.
- **Slide Show:** Memiliki fitur slide show yang menampilkan cover buku terbaru dan populer secara bergantian.
- **Navbar:** Menyediakan tombol navigasi yang berisi:
  - Home: Mengarahkan pengguna ke halaman utama.
  - Favorite: Mengarahkan pengguna ke daftar buku favorit.
  - Search: Mengarahkan pengguna ke halaman pencarian buku.
  - Profile: Mengarahkan pengguna ke halaman profil.
- **Nama Pengguna:** Menampilkan nama pengguna yang sedang login di pojok kanan atas untuk memberikan sentuhan personal.

**3. Detail Buku:**
- **Cover Buku:** Menampilkan gambar sampul buku.
- **Judul Buku:** Menampilkan judul lengkap buku.
- **Rank:** Menampilkan peringkat buku berdasarkan popularitas.
- **Author:** Menampilkan nama penulis buku.
- **Publisher:** Menampilkan penerbit buku.
- **Summary:** Menyediakan ringkasan singkat tentang isi buku.
- **Recommedation:** Menyajikan rekomendasi buku lain yang serupa dengan buku yang sedang dilihat.
- **Favorite:** Tombol untuk menambahkan atau menghapus buku dari daftar favorit.
- **Tombol Back:** Mengarahkan kembali ke halaman sebelumnya.
- **Read More:** Tombol yang membawa pengguna ke URL pembelian buku di Amazon untuk informasi lebih lanjut dan pembelian.

**4. Favorite:**
- **Daftar Favorit:** Menampilkan semua buku yang telah ditandai sebagai favorit oleh pengguna. Pengguna dapat melihat dan menghapus buku dari daftar favorit.
- **Detail Buku:** Menampilkan detail buku yang disimpan di favorit, termasuk cover buku, judul, rank, author, dan publisher.

**5. Search:**
- **Pencarian Buku:** Memungkinkan pengguna untuk mencari buku dengan mengetikkan judul lengkap buku yang ingin dicari. Hasil pencarian akan menampilkan daftar buku yang sesuai dengan kata kunci.

**6. Profile:**
- **Data Pengguna:** Menampilkan informasi pengguna seperti nama lengkap, gender, nomor HP, alamat, dan genre favorit.
- **Edit Profil:** Pengguna dapat mengedit informasi pribadi mereka.
- **Hapus Akun:** Opsi untuk menghapus akun pengguna jika diinginkan.
- **Logout:** Tombol untuk keluar dari akun pengguna.

Dengan fitur-fitur ini, aplikasi ReadMe! memberikan pengalaman yang lengkap dan menyenangkan bagi para penggemar buku dalam menjelajahi, menemukan, dan mengelola bacaan mereka.

# Penggunaan

**Login:**
Jika belum memiliki akun, pengguna dapat melakukan registrasi terlebih dahulu dengan mengisi formulir pendaftaran. Setelah itu, pengguna bisa login dengan memasukkan username dan password yang telah didaftarkan.

**Akses Buku:**
Pengguna akan diarahkan ke halaman Home setelah login. Di halaman ini, pengguna bisa melihat berbagai judul buku yang tersedia. Untuk melihat detail tentang buku, pengguna dapat mengklik judul buku yang diinginkan. Pengguna juga dapat menambahkan buku ke daftar favorit mereka. Jika ingin membeli buku atau mendapatkan informasi lebih lanjut, pengguna dapat menekan tombol "Read More" yang akan mengarahkan ke halaman pembelian di Amazon.

**Search:**
Jika pengguna ingin mencari buku tertentu, mereka dapat menggunakan fitur pencarian dengan mengetikkan judul buku yang diinginkan. Hasil pencarian akan menampilkan buku yang sesuai dengan kata kunci yang dimasukkan.

**Profile:**
Disarankan agar pengguna melengkapi data yang kurang di halaman profil, seperti nama lengkap, gender, dan nomor HP. Di halaman profil, pengguna juga memiliki opsi untuk logout dari akun mereka atau menghapus akun jika diinginkan.


# Software/Pendukung Aplikasi

* SQLite : digunakan untuk menyimpan data pengguna dan informasi terkait aplikasi lainnya.
* Retrofit : untuk pengambilan API https://rapidapi.com/yoginoit39/api/all-books-api/ 
* Androdi Studio : untuk pengembangan Aplikasi
* Lottie : Untuk progressbar

## License

[MIT](https://choosealicense.com/licenses/mit/)
