// 이미지 미리보기 기능
const productImageInput = document.getElementById('productImage');
const imagePreviewContainer = document.getElementById('imagePreview');

productImageInput.addEventListener('change', function() {
    imagePreviewContainer.innerHTML = ''; // 기존 미리보기 초기화

    const files = this.files;

    if (files) {
        Array.from(files).forEach(file => {
            const reader = new FileReader();

            reader.onload = function(e) {
                const imgElement = document.createElement('img');
                imgElement.src = e.target.result;
                imagePreviewContainer.appendChild(imgElement);
            };

            reader.readAsDataURL(file);
        });
    }
});