$(document).ready(function () {
    initSortableTables();
});

function initSortableTables() {
    $('table.sortable-table').each(function () {
        const $table = $(this);
        $table.find('th').each(function (index) {
            const $header = $(this);
            const $indicator = $('<span>')
                .addClass('sort-indicator')
                .appendTo($header)
            $header.on('click', function () {
                sortTable($table, index);
            })
        })
    })
}

function sortTable($table, columnIndex) {
    const sortDirection = getSortDirection($table, columnIndex);
    const $tbody = $table.find('tbody');
    const $rows = $tbody.find('tr').get();

    $rows.sort((rowA, rowB) => {
        const cellA = $(rowA).find('td').eq(columnIndex).text().trim();
        const cellB = $(rowB).find('td').eq(columnIndex).text().trim();

        const isNumeric = !isNaN(parseFloat(cellA)) && !isNaN(parseFloat(cellB));

        if (isNumeric) {
            return sortDirection * (parseFloat(cellA) - parseFloat(cellB));
        } else {
            return sortDirection * cellA.localeCompare(cellB);
        }
    });

    $tbody.empty()
    $.each($rows, function (index, row) {
        $tbody.append(row)
    })
}

function getSortDirection($table, columnIndex) {
    const $header = $table.find('th').eq(columnIndex);
    const $indicator = $header.find('.sort-indicator');

    if ($indicator.text() === '^') {
        resetSortIndicators($table);
        $indicator.text('V');
        return -1;
    } else {
        resetSortIndicators($table);
        $indicator.text('^');
        return 1;
    }
}

function resetSortIndicators($table) {
    $table.find('.sort-indicator').text('')
}