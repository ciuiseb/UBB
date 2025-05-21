document.addEventListener('DOMContentLoaded', function() {
    initSortableTables();
});

function initSortableTables() {
    const tables = document.querySelectorAll('table.sortable-table');

    tables.forEach(table => {
        const headers = table.querySelectorAll('th');

        headers.forEach((header, index) => {
            const indicator = document.createElement('span');
            indicator.className = 'sort-indicator';
            header.appendChild(indicator);

            header.addEventListener('click', function() {
                sortTable(table, index);
            });
        });
    });
}

function sortTable(table, columnIndex) {
    const sortDirection = getSortDirection(table, columnIndex);
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));

    rows.sort((rowA, rowB) => {
        const cellA = rowA.querySelectorAll('td')[columnIndex].textContent.trim();
        const cellB = rowB.querySelectorAll('td')[columnIndex].textContent.trim();

        const isNumeric = !isNaN(parseFloat(cellA)) && !isNaN(parseFloat(cellB));

        if (isNumeric) {
            return sortDirection * (parseFloat(cellA) - parseFloat(cellB));
        } else {
            return sortDirection * cellA.localeCompare(cellB);
        }
    });

    while (tbody.firstChild) {
        tbody.removeChild(tbody.firstChild);
    }

    rows.forEach(row => {
        tbody.appendChild(row);
    });
}

function getSortDirection(table, columnIndex) {
    const headers = table.querySelectorAll('th');
    const header = headers[columnIndex];
    const indicator = header.querySelector('.sort-indicator');

    if (indicator.textContent === '^') {
        resetSortIndicators(table);
        indicator.textContent = 'V';
        return -1;
    } else {
        resetSortIndicators(table);
        indicator.textContent = '^';
        return 1;
    }
}

function resetSortIndicators(table) {
    const indicators = table.querySelectorAll('.sort-indicator');
    indicators.forEach(indicator => {
        indicator.textContent = '';
    });
}