package com.braintrader.datamanagement;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RowKey implements Comparable<RowKey> {

    private final LocalDate date;
    private final String symbol;

    public RowKey(LocalDate date, String symbol) {
        this.date = date;
        this.symbol = symbol;
    }

    @Override
    public int compareTo(RowKey o) {

        int dateComparison = this.date.compareTo(o.date);

        if (dateComparison != 0) {
            return dateComparison;
        }

        return this.symbol.compareTo(o.symbol);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RowKey rowKey = (RowKey) o;

        if (!date.equals(rowKey.date)) {
            return false;
        }

        return symbol.equals(rowKey.symbol);

    }

    @Override
    public int hashCode() {

        int result = date.hashCode();
        result = 31 * result + symbol.hashCode();

        return result;

    }


}
