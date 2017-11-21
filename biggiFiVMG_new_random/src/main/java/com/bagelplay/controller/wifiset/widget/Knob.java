package com.bagelplay.controller.wifiset.widget;

import android.util.Log;

public class Knob {

    public static final int CLOCKWISE = 1;

    public static final int ANTICLOCKWISE = 2;

    private int centerX;

    private int centerY;

    private int r;

    private int lx;

    private int ly;

    private OnClockwiseListener ocl;

    private int degree;

    public Knob() {

    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setCenter(int x, int y) {
        this.centerX = x;
        this.centerY = y;
    }

    public void setLastXY(int lx, int ly) {
        if (!canTurn(lx, ly))
            return;
        this.lx = lx;
        this.ly = ly;
    }

    public void setXY(int x, int y) {
        if (!canTurn(x, y) || !canTurn(lx, ly))
            return;
        int degree1 = getDegree(lx, ly);
        int degree2 = getDegree(x, y);
        Log.v("=----------------------------=", degree1 + "     " + degree2);
         /*double sin1	=	(ly + 0f - centerY) / r;
		double sin2	=	(y + 0f - centerY) / r;
		double degreeSin1	=	Math.asin(sin1);
		if()
		
		double degreeSin1	=	lx > centerX ? Math.asin(sin1) : 180 - Math.asin(sin1);
		
		double cDegree	=	Math.toDegrees(Math.asin(sin2) - Math.asin(sin1));*/
        if (degree1 != degree2 && degree1 > 270 && degree2 > 0 && degree2 < 90) {
            if (degree2 + 360 - degree1 >= degree) {
                doClockwise(1);
                lx = x;
                ly = y;
            }
        } else if (degree1 != degree2 && degree1 > 0 && degree1 < 90 && degree2 > 270) {
            if (degree1 - (degree2 - 360) >= degree) {
                doClockwise(-1);
                lx = x;
                ly = y;
            }

        } else if (Math.abs(degree2 - degree1) >= degree) {
            doClockwise(degree2 - degree1);
            lx = x;
            ly = y;
        }


    }

    public void setOnClockwiseListener(OnClockwiseListener ocl) {
        this.ocl = ocl;
    }

    private void doClockwise(double cDegree) {
        if (ocl == null)
            return;
        if (cDegree > 0) {
            ocl.OnClockwise(ANTICLOCKWISE);
        } else {
            ocl.OnClockwise(CLOCKWISE);
        }
    }

    private boolean canTurn(int x, int y) {
        int tr = (int) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return tr > 0 && tr < r;
    }

    public interface OnClockwiseListener {
        public void OnClockwise(int clockwise);
    }

    private int getDegree(int x, int y) {
        if (x == centerX && y > centerY)
            return 90;
        if (x == centerX && y < centerY)
            return 270;
        if (y == centerY && x > centerX)
            return 0;
        if (y == centerY && x < centerX)
            return 180;
        double sin = (Math.abs(y + 0f - centerY)) / r;
        double degree = Math.toDegrees(Math.asin(sin));
        if (x > centerX && y < centerY)
            return (int) degree;
        if (x < centerX && y < centerY)
            return 180 - (int) degree;
        if (x < centerX && y > centerY)
            return 270 - (int) degree;
        if (x > centerX && y > centerY)
            return 360 - (int) degree;
        return 0;


    }

}
