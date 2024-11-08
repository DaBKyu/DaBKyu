
/*
// 메뉴 아이콘 클릭 시 메뉴를 열거나 닫기
document.getElementById('menu-toggle').addEventListener('click', function() {
    const aside = document.querySelector('aside');
    aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기

});
*/
/*
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.getElementById('menu-toggle');
    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            const aside = document.querySelector('aside');
            aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기
        });
    } else {
        console.error("menu-toggle 요소를 찾을 수 없습니다.");
    }
});
*/
// const menuToggle = document.querySelector('menu-toggle');
// menuToggle.addEventListener('click', function() {
//     const aside = document.querySelector('aside');
//     aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기
// });
// });

    const menuToggle = document.querySelector('menu-toggle');
        menuToggle.addEventListener('click', function() {
            const aside = document.querySelector('aside');
            aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기
        });