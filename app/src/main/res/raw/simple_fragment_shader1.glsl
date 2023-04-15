precision mediump float;

//注意：fragment中使用varying声明的字段名，必须与vertex中使用varying声明的字段名完全一直，大小写也要一样
varying vec4 v_Color;

void main() {
    gl_FragColor = v_Color;
}
