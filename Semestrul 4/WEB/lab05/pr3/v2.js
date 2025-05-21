window.initImageGame = function () {
    const $gameBoard = $('#game-board');

    let cards = [];
    let flippedCards = [];
    let canFlip = true;

    $gameBoard.addClass('image-grid');

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
        $gameBoard.text('');
        flippedCards = [];
        canFlip = true;

        cards = createImageCards();

        $.each(cards, function (i, index) {
            let imageNumber = i + 1
            const card = $('<div>')
                .addClass('card')
                .attr('data-index', index)
                .attr('data-value', Math.floor(index / 2))
                .on('click', flipCard);
            const img = $('<img>')
                .attr('alt', 'Card ' + imageNumber)
                .attr('src', 'images/image' + card.data('value') + '.jpg');
            card.append(img)
                .appendTo($gameBoard)

        });
    }

    function flipCard() {
        if (!canFlip) return;

        const $selectedCard = $(this);

        if (flippedCards.length === 2 ||
            $selectedCard.hasClass('flipped') ||
            $selectedCard.hasClass('matched')) {
            return;
        }

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
            flippedCards[0].removeClass('flipped');
            flippedCards[1].removeClass('flipped');

            flippedCards = [];
            canFlip = true;
        }, 750);
    }

    startGame();
};


if (window.showImageGame !== false) {
    $(document).ready(function () {
        window.initImageGame();
    });
}