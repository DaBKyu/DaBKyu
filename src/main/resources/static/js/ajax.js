const track = document.querySelector('.carousel-track');
    const slides = Array.from(track.children);
    const nextButton = document.querySelector('.next');
    const prevButton = document.querySelector('.prev');

    // 각 슬라이드의 너비 구하기 (상품 4개 기준)
    const slideWidth = slides[0].getBoundingClientRect().width;

    // 슬라이드가 움직일 때마다 위치를 변경하도록 하는 함수
    const moveToSlide = (index) => {
        track.style.transform = `translateX(-${index * slideWidth}px)`;

        // 슬라이드가 마지막에 도달했을 때 버튼 숨기기
        if (index === slides.length - 1) {
            nextButton.style.display = 'none'; // 마지막 슬라이드에서는 next 버튼 숨기기
        } else {
            nextButton.style.display = 'block'; // 마지막 슬라이드가 아니면 next 버튼 보이기
        }

        // 슬라이드가 첫 번째에 도달했을 때 버튼 숨기기
        if (index === 0) {
            prevButton.style.display = 'none'; // 첫 번째 슬라이드에서는 prev 버튼 숨기기
        } else {
            prevButton.style.display = 'block'; // 첫 번째 슬라이드가 아니면 prev 버튼 보이기
        }
    };

    // 현재 슬라이드 인덱스 초기화
    let currentSlideIndex = 0;

    // 첫 번째 슬라이드가 보일 때 prev 버튼 숨기기
    prevButton.style.display = 'none';

    // 슬라이드 전환 (다음 버튼)
    nextButton.addEventListener('click', () => {
        if (currentSlideIndex < slides.length - 1) {
            currentSlideIndex++;
        } else {
            currentSlideIndex = 0; // 마지막 슬라이드에서 첫 번째 슬라이드로 돌아가기
        }
        moveToSlide(currentSlideIndex);
    });

    // 슬라이드 전환 (이전 버튼)
    prevButton.addEventListener('click', () => {
        if (currentSlideIndex > 0) {
            currentSlideIndex--;
        } else {
            currentSlideIndex = slides.length - 1; // 첫 번째 슬라이드에서 마지막 슬라이드로 돌아가기
        }
        moveToSlide(currentSlideIndex);
    });
