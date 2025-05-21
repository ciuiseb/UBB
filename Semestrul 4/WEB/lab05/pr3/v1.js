window.initNumberGame = function () {
    const $gameBoard = $('#game-board');

    let cards = [];
    let flippedCards = [];
    let canFlip = true;

    $gameBoard.addClass('number-grid');

    function createNumberCards() {
        return shuffleArray([...Array(16).keys()]);
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
        $gameBoard.empty();
        flippedCards = [];
        canFlip = true;

        cards = createNumberCards();

        $.each(cards, function (index) {
            $('<div>')
                .addClass('card')
                .attr('data-index', index)
                .attr('data-value', Math.floor(index / 2))
                .on('click', flipCard)
                .appendTo($gameBoard)
        });
    }

    function flipCard() {
        if (!canFlip) return;

        const $selectedCard = $(this);
        const cardIndex = $selectedCard.data('index');

        if (flippedCards.length === 2 ||
            $selectedCard.hasClass('flipped') ||
            $selectedCard.hasClass('matched')) {
            return;
        }

        $selectedCard.text($selectedCard.data('value'));
        $selectedCard.addClass('flipped');
        flippedCards.push($selectedCard);

        if (flippedCards.length === 2) {
            const firstCardValue = flippedCards[0].data('value');
            const secondCardValue = flippedCards[1].data('value');

            if (firstCardValue === secondCardValue) {
                matchedCards();
            } else {
                hideCards();
            }
        }
    }

    function matchedCards() {
        flippedCards[0].addClass('matched');
        flippedCards[1].addClass('matched');

        flippedCards = [];
    }

    function hideCards() {
        canFlip = false;

        setTimeout(() => {
            flippedCards[0].text('');
            flippedCards[1].text('');

            flippedCards[0].removeClass('flipped');
            flippedCards[1].removeClass('flipped');

            flippedCards = [];
            canFlip = true;
        }, 750);
    }

    startGame();
};

if (window.showNumberGame !== false) {
    $(document).ready(function () {
        window.initNumberGame();
    });
}