window.initImageGame = function() {
    const gameBoard = document.getElementById('game-board');

    let cards = [];
    let flippedCards = [];
    let canFlip = true;

    gameBoard.classList.add('number-grid');

    function createImageCards() {
        return shuffleArray([...Array(16).keys()]);
    }

    function shuffleArray(array) {
        let currentIndex = array.length;
        let temporaryValue, randomIndex;

        while (currentIndex !== 0) {
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            temporaryValue = array[currentIndex];
            array[currentIndex] = array[randomIndex];
            array[randomIndex] = temporaryValue;
        }

        return array;
    }

    function startGame() {
        gameBoard.innerHTML = '';
        flippedCards = [];
        canFlip = true;

        cards = createImageCards();

        cards.forEach((index) => {
            const card = document.createElement('div');
            card.classList.add('card');
            card.dataset.index = index;
            card.dataset.value = Math.floor(index / 2);

            const img = document.createElement('img');

            const imageNumber = parseInt(card.dataset.value) % 8 + 1;
            img.alt = `Image ${imageNumber}`;
            img.src = `images/image${imageNumber}.jpg`;

            card.appendChild(img);
            card.addEventListener('click', flipCard);

            gameBoard.appendChild(card);
        });
    }

    function flipCard() {
        if (!canFlip) return;

        const selectedCard = this;

        if (flippedCards.length === 2 || selectedCard.classList.contains('flipped') ||
            selectedCard.classList.contains('matched')) {
            return;
        }

        selectedCard.classList.add('flipped');
        flippedCards.push(selectedCard);

        if (flippedCards.length === 2) {
            const firstCardValue = flippedCards[0].dataset.value;
            const secondCardValue = flippedCards[1].dataset.value;

            if (firstCardValue === secondCardValue) {
                matchedCards();
            } else {
                hideCards();
            }
        }
    }

    function matchedCards() {
        flippedCards[0].classList.add('matched');
        flippedCards[1].classList.add('matched');

        flippedCards = [];
    }

    function hideCards() {
        canFlip = false;

        setTimeout(() => {
            flippedCards[0].classList.remove('flipped');
            flippedCards[1].classList.remove('flipped');

            flippedCards = [];
            canFlip = true;
        }, 750);
    }



    startGame();
};
