package com.joechamm.openglenvironment;

public class SparseMatrix {

    protected float[][] nzValues;
    protected int[][] columnIndices;
    protected int[] nzCounters;
    public int colCount = 0;
    public int rowCount = 0;

    public SparseMatrix ( int colCount, int rowCount ) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.nzValues = new float[ rowCount ][];
        this.columnIndices = new int[ rowCount ][];
        this.nzCounters = new int[ rowCount ];
    }

    private static int binarySearch (
            int[] array, int startIdx, int endIdx,
            int value
    ) {
        if ( value < array[ startIdx ] ) {
            return ( - startIdx - 1 );
        }

        if ( value > array[ endIdx ] ) {
            return ( - ( endIdx + 1 ) - 1 );
        }

        if ( startIdx == endIdx ) {
            if ( array[ startIdx ] == value ) {
                return startIdx;
            } else {
                return ( - ( startIdx + 1 ) - 1 );
            }
        }

        int midIdx = ( startIdx + endIdx ) / 2;
        if ( value == array[ midIdx ] ) {
            return midIdx;
        }

        if ( value < array[ midIdx ] ) {
            return binarySearch ( array, startIdx, midIdx - 1, value );
        } else {
            return binarySearch ( array, midIdx + 1, endIdx, value );
        }
    }

    public float get ( int column, int row ) {
        if ( this.columnIndices[ row ] == null ) {
            return 0.0f;
        }
        int columnIdx = binarySearch ( this.columnIndices[ row ], 0,
                                       this.nzCounters[ row ] - 1, column );

        if ( columnIdx < 0 ) {
            return 0.0f;
        }
        return this.nzValues[ row ][ columnIdx ];
    }

    public void set ( int column, int row, float value ) {
        if ( this.columnIndices[ row ] == null ) {
            // first value in this row
            this.columnIndices[ row ] = new int[ 2 ];
            this.nzValues[ row ] = new float[ 2 ];
            this.columnIndices[ row ][ 0 ] = column;
            this.nzValues[ row ][ 0 ] = value;
            this.nzCounters[ row ] = 1;
            return;
        }

        // search for it
        int colIndex = binarySearch ( this.columnIndices[ row ], 0,
                                      this.nzCounters[ row ] - 1, column );
        if ( colIndex >= 0 ) {
            this.nzValues[ row ][ colIndex ] = value;
            return;
        } else {
            int insertionPoint = - ( colIndex + 1 );
            int oldLength = this.nzCounters[ row ];
            int newLength = oldLength + 1;
            if ( newLength <= this.columnIndices[ row ].length ) {
                if ( insertionPoint != oldLength ) {
                    for ( int i = oldLength; i > insertionPoint; i-- ) {
                        this.nzValues[ row ][ i ] = this.nzValues[ row ][ i - 1 ];
                        this.columnIndices[ row ][ i ] = this.columnIndices[ row ][ i - 1 ];
                    }
                }

                this.columnIndices[ row ][ insertionPoint ] = column;
                this.nzValues[ row ][ insertionPoint ] = value;
                this.nzCounters[ row ]++;
                return;
            }

            int[] newColumnIndices = new int[ 2 * oldLength ];
            float[] newNzValues = new float[ 2 * oldLength ];
            if ( insertionPoint == oldLength ) {
                System.arraycopy ( this.columnIndices[ row ], 0, newColumnIndices,
                                   0, oldLength );
                System.arraycopy ( this.nzValues[ row ], 0, newNzValues, 0,
                                   oldLength );
            } else {
                System.arraycopy ( this.columnIndices[ row ], 0, newColumnIndices,
                                   0, insertionPoint );
                System.arraycopy ( this.nzValues[ row ], 0, newNzValues, 0,
                                   insertionPoint );
                System.arraycopy ( this.columnIndices[ row ], insertionPoint,
                                   newColumnIndices, insertionPoint + 1, oldLength
                                           - insertionPoint );
                System.arraycopy ( this.nzValues[ row ], insertionPoint,
                                   newNzValues, insertionPoint + 1, oldLength
                                           - insertionPoint );
            }

            newColumnIndices[ insertionPoint ] = column;
            newNzValues[ insertionPoint ] = value;
            this.columnIndices[ row ] = null;
            this.columnIndices[ row ] = newColumnIndices;
            this.nzValues[ row ] = null;
            this.nzValues[ row ] = newNzValues;
            this.nzCounters[ row ]++;
        }
    }

    public void dump () {
        System.out.println ( "MATRIX " + this.rowCount + "*" + this.colCount );
        for ( int row = 0; row < this.rowCount; row++ ) {
            int[] columnIndices = this.columnIndices[ row ];
            if ( columnIndices == null ) {
                for ( int col = 0; col < this.colCount; col++ ) {
                    System.out.print ( "0.0 " );
                }
            } else {
                int prevColumnIndex = 0;
                for ( int colIndex = 0; colIndex < this.nzCounters[ row ]; colIndex++ ) {
                    int currColumnIndex = columnIndices[ colIndex ];
                    for ( int col = prevColumnIndex; col < currColumnIndex; col++ ) {
                        System.out.print ( "0.0 " );
                        ;
                    }
                    System.out.print ( this.nzValues[ row ][ colIndex ] + " " );
                    prevColumnIndex = currColumnIndex + 1;
                }

                for ( int col = prevColumnIndex; col < this.colCount; col++ ) {
                    System.out.print ( "0.0 " );
                }
            }
            System.out.println ();
        }
    }

    public void dumpInt () {
        System.out.println ( "MATRIX " + this.rowCount + "*" + this.colCount );
        for ( int row = 0; row < this.rowCount; row++ ) {
            int[] columnIndices = this.columnIndices[ row ];
            if ( columnIndices == null ) {
                for ( int col = 0; col < this.colCount; col++ ) {
                    System.out.print ( "0 " );
                }
            } else {
                int prevColumnIndex = 0;
                for ( int colIndex = 0; colIndex < this.nzCounters[ row ]; colIndex++ ) {
                    int currColumnIndex = columnIndices[ colIndex ];
                    // put zeroes
                    for ( int col = prevColumnIndex; col < currColumnIndex; col++ ) {
                        System.out.print ( "0 " );
                    }
                    System.out.print ( (int) this.nzValues[ row ][ colIndex ] + " " );
                    prevColumnIndex = currColumnIndex + 1;
                }
                // put trailing zeroes
                for ( int col = prevColumnIndex; col < this.colCount; col++ ) {
                    System.out.print ( "0 " );
                }
            }

            System.out.println ();
        }

    }

    public void addEmptyColumns ( int columns ) {
        this.colCount += columns;
    }

    public float[] mult ( float[] vector ) {
        if ( this.colCount != vector.length ) {
            return null;
        }

        int n = this.rowCount;
        float[] result = new float[ n ];
        for ( int row = 0; row < n; row++ ) {
            float sum = 0.0f;
            // go over all non-zero column of this row
            int[] nzIndexes = this.columnIndices[ row ];
            int nzLength = nzCounters[ row ];
            if ( nzLength == 0 ) {
                continue;
            }

            for ( int colIndex = 0; colIndex < nzLength; colIndex++ ) {
                float c = vector[ nzIndexes[ colIndex ] ];
                sum += ( this.nzValues[ row ][ colIndex ] * c );
            }
            result[ row ] = sum;
        }
        return result;
    }

    public float getSum ( int row ) {
        float sum = 0.0f;
        // go over all non-zero column of this row
        int nzLength = nzCounters[ row ];
        if ( nzLength == 0 ) {
            return 0.0f;
        }

        for ( int colIndex = 0; colIndex < nzLength; colIndex++ ) {
            sum += this.nzValues[ row ][ colIndex ];
        }

        return sum;
    }

    public float getSum ( int row, boolean[] toConsider ) {
        float sum = 0.0f;
        int nzLength = nzCounters[ row ];
        if ( nzLength == 0 ) {
            return 0.0f;
        }

        for ( int colIndex = 0; colIndex < nzLength; colIndex++ ) {
            if ( toConsider[ this.columnIndices[ row ][ colIndex ] ] ) {
                sum += this.nzValues[ row ][ colIndex ];
            }
        }
        return sum;
    }

    public int getNzCount () {
        int allNz = 0;
        for ( int i : this.nzCounters ) {
            allNz += i;
        }
        return allNz;
    }

    public void normalize () {
        float minValue = 0.0f;
        float maxValue = 0.0f;
        boolean isFirst = true;
        for ( int i = 0; i < this.rowCount; i++ ) {
            if ( this.nzCounters[ i ] == 0 ) {
                continue;
            }

            for ( int j = 0; j < this.nzCounters[ i ]; j++ ) {
                float val = this.nzValues[ i ][ j ];
                if ( isFirst ) {
                    minValue = val;
                    maxValue = val;
                    isFirst = false;
                } else {
                    if ( val < minValue ) {
                        minValue = val;
                    }
                    if ( val > maxValue ) {
                        maxValue = val;
                    }
                }
            }
        }

        for ( int i = 0; i < this.rowCount; i++ ) {
            if ( this.nzCounters[ i ] == 0 ) {
                continue;
            }
            for ( int j = 0; j < this.nzCounters[ i ]; j++ ) {
                this.nzValues[ i ][ j ] /= maxValue;
            }
        }
    }

}
