attribute vec4 a_Position;
attribute vec4 a_Color;

//注意：fragment中使用varying声明的字段名，必须与vertex中使用varying声明的字段名完全一直，大小写也要一样
varying vec4 v_Color;

void main() {

    gl_Position = a_Position;
    //设置点的大小
    gl_PointSize = 10.0;


    v_Color = a_Color;
}
