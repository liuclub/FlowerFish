/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_bagelplay_controller_wifiset_Jni */

#include <math.h>
#include <poll.h>
#include <stdio.h>
#include <stdarg.h>
#include <fcntl.h>
#include <errno.h>
#include <netdb.h>
#include <stdint.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/timeb.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <linux/fb.h>
#include <sys/types.h>
#include <linux/tcp.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <linux/input.h>
#include <netinet/ip_icmp.h>

 
#include <net/if_arp.h>;
#include <arpa/inet.h>;
 

#ifndef _Included_com_bagelplay_controller_wifiset_Jni
#define _Included_com_bagelplay_controller_wifiset_Jni
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_bagelplay_controller_wifiset_Jni
 * Method:    findSticksFromWifi
 * Signature: ([Ljava/lang/String;)[[B
 */
JNIEXPORT jobjectArray JNICALL Java_com_bagelplay_controller_wifiset_Jni_findSticksFromWifi
  (JNIEnv *, jclass, jobjectArray);

JNIEXPORT void JNICALL Java_com_bagelplay_controller_wifiset_Jni_stop
  (JNIEnv *env, jclass c);

#ifdef __cplusplus
}
#endif
#endif