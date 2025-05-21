window.initNumberGame = function() {
    const gameBoard = document.getElementById('game-board');

    let cards = [];
    let flippedCards = [];
    let canFlip = true;

    gameBoard.classList.add('number-grid');

    function createNumberCards() {
        const numbers = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8];
        return shuffleArray(numbers);
    }

    function shuffleArray(array) {
        let currentIndex = array.length;
        let temp, randomIndex;

        while (currentIndex !== 0) {
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex -= 1;

            temp = array[currentIndex];
            array[currentIndex] = array[randomIndex];
            array[randomIndex] = temp;
        }

        return array;
    }

    function startGame() {
        gameBoard.innerHTML = '';
        flippedCards = [];
        canFlip = true;

        cards = createNumberCards();

        cards.forEach((number, index) => {
            const card = document.createElement('div');
            card.classList.add('card');
            card.dataset.index = index;
            card.dataset.value = number;

            card.addEventListener('click', flipCard);

            gameBoard.appendChild(card);
        });
    }

    function flipCard() {
        if (!canFlip) return;

        const selectedCard = this;
        const cardIndex = selectedCard.dataset.index;

        if (flippedCards.length === 2 || selectedCard.classList.contains('flipped') ||
            selectedCard.classList.contains('matched')) {
            return;
        }

        selectedCard.textContent = selectedCard.dataset.value;
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
            flippedCards[0].textContent = '';
            flippedCards[1].textContent = '';

            flippedCards[0].classList.remove('flipped');
            flippedCards[1].classList.remove('flipped');

            flippedCards = [];
            canFlip = true;
        }, 750);
    }
    startGame();
};

if (window.showNumberGame !== false) {
    document.addEventListener('DOMContentLoaded', function() {
        window.initNumberGame();
    });
}