/* DO NOT EDIT THIS FILE - it is machine generated */
#include "com_bagelplay_controller_wifiset_random_Jni.h"
/* Header for class com_bagelplay_controller_wifiset_Jni */

#define MAX_NUM  10

char stop = 0;

int hostNameLeng	=	200;

int port	=	17890;

jint JNI_OnLoad(JavaVM* vm,void *reserved)
{
	return JNI_VERSION_1_4;
}

int compare2Sticks(char * stick1,char * stick2)
{
	char *firstIndexC1=strchr(stick1,'|');
	char *lastIndexC1 = strrchr(stick1,'|');
	int rlmLen1 = lastIndexC1 - firstIndexC1 - 1;
	char realLN1[rlmLen1];
	memcpy(realLN1,firstIndexC1 + 1,rlmLen1);

	char *firstIndexC2=strchr(stick2,'|');
	char *lastIndexC2 = strrchr(stick2,'|');
	int rlmLen2 = lastIndexC2 - firstIndexC2 - 1;
	char realLN2[rlmLen2];
	memcpy(realLN2,firstIndexC2 + 1,rlmLen2);

	if(rlmLen1 != rlmLen2)
		return 1;
	int i;
	for(i=0;i<rlmLen1;i++)
	{
		if(realLN1[i] != realLN2[i])
			return 2;
	}
	return 0;
}


void getBrocastIp(int api_serv_socket_fd,char bip[])
{
	struct sockaddr_in sin;
	struct ifreq ifr;
	char *ETH_NAME = "eth0";
	strncpy(ifr.ifr_name, ETH_NAME, IFNAMSIZ);
	ioctl(api_serv_socket_fd, SIOCGIFADDR, &ifr);
	memcpy(&sin, &ifr.ifr_addr, sizeof(sin));
	char *ip = inet_ntoa(sin.sin_addr);
	char *ip1 = strrchr(ip,'.');
	memcpy(bip,ip,(ip1 - ip));
	strcpy(bip + (ip1 - ip),".255");
}

void initUdpBroadcast(struct sockaddr_in *api_cmd_addr,int *api_serv_socket_fd)
{
	int ret,yes = 1;
	*api_serv_socket_fd = socket (AF_INET, SOCK_DGRAM, 0);
	fcntl(*api_serv_socket_fd, F_SETFL, O_NONBLOCK);
	setsockopt(*api_serv_socket_fd, SOL_SOCKET, SO_BROADCAST, &yes, sizeof(yes));
    api_cmd_addr->sin_family = AF_INET;
    api_cmd_addr->sin_port = htons(port);
	//char brocastIp[20];
	//getBrocastIp(api_serv_socket_fd,brocastIp);
    api_cmd_addr->sin_addr.s_addr = htonl(INADDR_BROADCAST); //inet_addr(brocastIp);  
}

 
jobjectArray newSticks(JNIEnv *env,char sticks[MAX_NUM][hostNameLeng],int oldStickIndex,int stickIndex)
{
	int size = stickIndex - oldStickIndex;
	if(size == 0)
		return NULL;
	jclass byteArrCls = (*env)->FindClass(env,"[B");
	jobjectArray result	=	(*env)->NewObjectArray(env,size,byteArrCls,NULL);
	int i;
	for(i=0;i<size;i++)
	{
		char *s = sticks[oldStickIndex + i + 1];
		char *lastIndexC = strrchr(s,'|');
		int rlmLen = lastIndexC - s + 1;
		jbyteArray iarr=(*env)->NewByteArray(env,rlmLen);
		(*env)->SetByteArrayRegion(env,iarr,0,rlmLen,sticks[oldStickIndex + i + 1]);
		(*env)->SetObjectArrayElement(env,result,i,iarr);
		(*env)->DeleteLocalRef(env,iarr);
	}
	return result;
}

char * getIPFromStick(char *stickStr)
{
	char* indexc = strrchr(stickStr,'|');
	int leng = indexc - stickStr;
	char ip[leng];
	memcpy(ip,stickStr,leng);
	return ip;
}


jobjectArray getSticks(JNIEnv *env,int api_serv_socket_fd,struct sockaddr_in *api_cmd_addr,char sticks[MAX_NUM][hostNameLeng],int oldStickIndex)
{
	int sockaddr_in_leng = sizeof(struct sockaddr_in);
	char* buf = "SCN_RANDOM";
	jclass stringClass = (*env)->FindClass(env, "java/lang/String"); 
	int ret = sendto (api_serv_socket_fd, buf, 10, 0, api_cmd_addr, &sockaddr_in_leng);
	int hostnameLeng	=	hostNameLeng;
	char hostname[hostnameLeng];
	struct sockaddr_in api_cmd_addr2;
 	jobjectArray ja = NULL;
	int t1,t2;
	t1 = time((time_t*)NULL);	
	int stickIndex = oldStickIndex;
	while(1)
	{
		memset(hostname,0,hostnameLeng);
		memset(&api_cmd_addr2,0,sizeof(struct sockaddr_in));
		ret = recvfrom(api_serv_socket_fd, hostname, hostnameLeng, 0, (struct sockaddr*)&api_cmd_addr2, &sockaddr_in_leng);
		if(hostname[0] != 0)
		{
			struct sockaddr_in api_cmd_addr3;
			memcpy(&api_cmd_addr3,&api_cmd_addr2,sockaddr_in_leng);
			char *endIndex0 = strchr(hostname, NULL);
			int stickstrleng = endIndex0 - hostname;
			char sign = '|';
			memcpy(hostname + stickstrleng,&sign,1);
			char *ip = inet_ntoa(api_cmd_addr3.sin_addr);
			int ipleng = strlen(ip);
			memcpy(hostname + stickstrleng + 1,ip,ipleng);
			memcpy(hostname +stickstrleng + 1 + ipleng,&sign,1);
			
			char hostname2[hostnameLeng];
			memcpy(hostname2,hostname,hostnameLeng);
			int i;
			int have = 0;
			for(i=0;i<=stickIndex;i++)
			{
				if(compare2Sticks(hostname,sticks[i]) == 0)
				{
					have = 1;
					break;
				}
			}
			if(have == 0)
			{
				stickIndex++;
				stickIndex	=	stickIndex >= MAX_NUM ? MAX_NUM - 1 : stickIndex;
 				memcpy(sticks[stickIndex],hostname, strrchr(hostname,'|') - hostname + 1);
			}
		}
		t2 = time((time_t*)NULL);
		if(t2 > t1 && (t2 - t1) % 3 == 0)
		{
			if(stickIndex > oldStickIndex)
			{
				close(api_serv_socket_fd);
				break;
			}
			ret = sendto (api_serv_socket_fd, buf, 10, 0, api_cmd_addr, sizeof(struct sockaddr_in));
		}
		if(t2 - t1 >= 15)
		{
			close(api_serv_socket_fd);
			break;
		}
		//LOGI("---------stop----------------------------%d",stop);
		if(stop == 1)
		{
			close(api_serv_socket_fd);
			break;
		}
	}

	return newSticks(env,sticks,oldStickIndex,stickIndex);
}


JNIEXPORT jobjectArray JNICALL Java_com_bagelplay_controller_wifiset_random_Jni_findSticksFromWifi
  (JNIEnv *env, jclass c, jobjectArray ja)
  {
	stop	=	0;
	int api_serv_socket_fd; 
	struct sockaddr_in api_cmd_addr;  
	initUdpBroadcast(&api_cmd_addr,&api_serv_socket_fd); 
	char sticks[MAX_NUM][hostNameLeng];
	int stickIndex = -1;
	int leng = (*env)->GetArrayLength(env,ja);
	int i;
	for(i=0;i<leng;i++)
	{
		stickIndex++;
		stickIndex	=	stickIndex >= MAX_NUM ? MAX_NUM - 1 : stickIndex;
		jstring string = (jstring)(*env)->GetObjectArrayElement(env, ja, i);
		char *jastr = (*env)->GetStringUTFChars(env, string, 0);
		int charleng = (*env)->GetStringLength(env,string);
		memcpy(sticks[stickIndex],jastr,charleng);
		(*env)->ReleaseStringUTFChars(env,string,jastr);
	}
	return getSticks(env,api_serv_socket_fd,&api_cmd_addr,sticks,stickIndex);
  }
  
JNIEXPORT void JNICALL Java_com_bagelplay_controller_wifiset_random_Jni_stop
  (JNIEnv *env, jclass c)
{
	stop	=	1;
	LOGI("---------stop------%d",stop);
}
 