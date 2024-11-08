
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

//----------------------------------------------------------------------------------




 // ShopHeader가 로드된 후 menuToggle.js 기능 실행
//  const menuToggle = document.getElementById('menu-toggle');
//  const asideMenu = document.querySelector('aside');
//     if (menuToggle && asideMenu) {
//      menuToggle.addEventListener('click', function() {
//     asideMenu.classList.toggle('open');
//     });
//  };





// ShopHeader가 로드된 후 menuToggle.js 기능 실행
// const menuToggle = document.getElementById('menu-toggle');
// const asideMenu = document.querySelector('aside');

// if (menuToggle && asideMenu) {
//     menuToggle.addEventListener('click', function() {
//     asideMenu.classList.toggle('open');
//     });
//     };
    
    // const menuToggle = document.querySelector('menu-toggle');
    //     menuToggle.addEventListener('click', function() {
    //         const aside = document.querySelector('aside');
    //         aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기
    //     });

    //===================================================
    

///////////////////////////
// const track = document.querySelector('.carousel-track');
//     const slides = Array.from(track.children);
//     const nextButton = document.querySelector('.next');
//     const prevButton = document.querySelector('.prev');

//     // 각 슬라이드의 너비 구하기 (상품 4개 기준)
//     const slideWidth = slides[0].getBoundingClientRect().width;

//     // 슬라이드가 움직일 때마다 위치를 변경하도록 하는 함수
//     const moveToSlide = (index) => {
//         track.style.transform = `translateX(-${index * slideWidth}px)`;

//         // 슬라이드가 마지막에 도달했을 때 버튼 숨기기
//         if (index === slides.length - 1) {
//             nextButton.style.display = 'none'; // 마지막 슬라이드에서는 next 버튼 숨기기
//         } else {
//             nextButton.style.display = 'block'; // 마지막 슬라이드가 아니면 next 버튼 보이기
//         }

//         // 슬라이드가 첫 번째에 도달했을 때 버튼 숨기기
//         if (index === 0) {
//             prevButton.style.display = 'none'; // 첫 번째 슬라이드에서는 prev 버튼 숨기기
//         } else {
//             prevButton.style.display = 'block'; // 첫 번째 슬라이드가 아니면 prev 버튼 보이기
//         }
//     };

//     // 현재 슬라이드 인덱스 초기화
//     let currentSlideIndex = 0;

//     // 첫 번째 슬라이드가 보일 때 prev 버튼 숨기기
//     prevButton.style.display = 'none';

//     // 슬라이드 전환 (다음 버튼)
//     nextButton.addEventListener('click', () => {
//         if (currentSlideIndex < slides.length - 1) {
//             currentSlideIndex++;
//         } else {
//             currentSlideIndex = 0; // 마지막 슬라이드에서 첫 번째 슬라이드로 돌아가기
//         }
//         moveToSlide(currentSlideIndex);
//     });

//     // 슬라이드 전환 (이전 버튼)
//     prevButton.addEventListener('click', () => {
//         if (currentSlideIndex > 0) {
//             currentSlideIndex--;
//         } else {
//             currentSlideIndex = slides.length - 1; // 첫 번째 슬라이드에서 마지막 슬라이드로 돌아가기
//         }
//         moveToSlide(currentSlideIndex);
//     });

fetch('../layout/ShopHeader.html')
.then(response => response.text())
.then(data => {
document.getElementById('ShopHeader').innerHTML = data;

// 메뉴 아이콘 클릭 시 메뉴를 열거나 닫기
document.getElementById('menu-toggle').addEventListener('click', function() {
    const aside = document.querySelector('aside');
    aside.classList.toggle('open'); // 'open' 클래스를 토글하여 메뉴 열기/닫기
});

// 알림 버튼 및 모달 요소 가져오기
const notificationBtn = document.getElementById('notificationBtn');
const notificationModal = document.getElementById('notificationModal');

// 알림 버튼 클릭 시 모달 열기/닫기
notificationBtn.addEventListener('click', () => {
    if (notificationModal.style.display === 'block') {
        // 모달이 이미 열려 있으면 닫기
        notificationModal.style.display = 'none';
    } else {
        // 모달이 닫혀 있으면 열기
        notificationModal.style.display = 'block';
    }
});

// 모달 외부 클릭 시 모달 숨기기
window.addEventListener('click', (event) => {
    if (notificationModal.style.display === 'block' && 
        event.target !== notificationModal && 
        event.target !== notificationBtn && 
        !notificationModal.contains(event.target)) {
            notificationModal.style.display = 'none';
    }
})});