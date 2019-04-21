package ru.zavedyaev.fillme.shader

object Shader {
    val vertexShaderCode = """
        uniform mat4 uMVPMatrix;
        attribute vec4 aPosition;
        attribute vec4 aColor;
        attribute vec2 aTexCoordinate;

        varying vec4 vColor;
        varying vec2 vTexCoordinate;

        void main() {
            vColor = aColor;
            vTexCoordinate = aTexCoordinate;
            gl_Position = uMVPMatrix * aPosition;
        }
    """.trimIndent()

    val fragmentShaderCode = """
        precision mediump float;

        uniform sampler2D uTexture;

        varying vec4 vColor;
        varying vec2 vTexCoordinate;

        void main() {
            gl_FragColor = (vColor * texture2D(uTexture, vTexCoordinate));
        }
    """.trimIndent()
}