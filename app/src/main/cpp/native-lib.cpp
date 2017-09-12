#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_secbox_zhaoqc_secbox_NDKActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
