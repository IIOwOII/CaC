import mmap
import os

filename = "shared.dat"

# 1. 자바가 파일을 생성한 후 읽어야 함
if not os.path.exists(filename):
    print("File not found. Run Java code first.")

# 2. 파일 열기 및 매핑
with open(filename, "r+b") as f:
    # mmap.mmap(file_descriptor, length)
    with mmap.mmap(f.fileno(), 1024, access=mmap.ACCESS_WRITE) as mm:
        # 3. 데이터 읽기
        data = mm.read(15).decode('utf-8')
        print(f"Python: Data read from Java: {data}")

        # 4. 데이터 수정 (선택 사항)
        mm.seek(0)
        mm.write(b"Hello from Pyth")
        mm.flush() # 변경 사항 반영