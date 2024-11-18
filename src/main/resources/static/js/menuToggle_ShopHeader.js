
//----------- ShopHeader.html 확인할때만 주석처리 하기 ( 평상시는 주석 X )---------
fetch('../layout/ShopHeader.html')
.then(response => response.text())
.then(data => {
document.getElementById('ShopHeader').innerHTML = data;
//------------------------------------------------------------------------------


// 메뉴 아이콘 클릭 시 메뉴를 열거나 닫기
document.getElementById('menu-toggle').addEventListener('click', function() {
    const aside = document.querySelector('aside');
    aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기
});

// // 알림 버튼 및 모달 요소 가져오기
// const notificationBtn = document.getElementById('notificationBtn');
// const notificationModal = document.getElementById('notificationModal');

// // 알림 버튼 클릭 시 모달 열기/닫기
// notificationBtn.addEventListener('click', () => {
//     if (notificationModal.style.display === 'block') {
//         // 모달이 이미 열려 있으면 닫기
//         notificationModal.style.display = 'none';
//     } else {
//         // 모달이 닫혀 있으면 열기
//         notificationModal.style.display = 'block';
//     }
// });

// // 모달 외부 클릭 시 모달 숨기기
// window.addEventListener('click', (event) => {
//     if (notificationModal.style.display === 'block' && 
//         event.target !== notificationModal && 
//         event.target !== notificationBtn && 
//         !notificationModal.contains(event.target)) {
//             notificationModal.style.display = 'none';
//     }
// })
//----------- ShopHeader.html 확인할때만 주석처리 하기 ( 평상시는 주석 X )---------
})
//------------------------------------------------------------------------------
;

