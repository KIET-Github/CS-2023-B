from django.shortcuts import render
from django.http import JsonResponse
import random
import time
from agora_token_builder import RtcTokenBuilder
from .models import RoomMember
import json
from django.views.decorators.csrf import csrf_exempt



# import agora
# # import requests
# from django.http import JsonResponse

# # Initialize the Agora SDKs
# client = agora.AgoraRTC.create_watcher("00d80b93181c45f5ab2e33898164ce76", "d6c002e936ed4dccb928ec3481c67eb8")

# # Define a Django view to retrieve the video stream data
# def get_video_stream_data(request):
#     # Get the video stream data from Agora
#     stream_data = client.get_video_stream_data("129-227-205-80.edge.agora.io:4713")

#     # Send the stream data to your machine learning project for processing
#     ml_url = "http://<ml_project_url>/process_video_stream"
#     response ={}

#     # Return a JSON response with the result of the machine learning processing
#     return JsonResponse({"result": response.json()})




# Create your views here.

def lobby(request):
    return render(request, 'base/lobby.html')

def room(request):
    return render(request, 'base/room.html')

def getToken(request):
    print(request.GET)
    appId = "00d80b93181c45f5ab2e33898164ce76"
    appCertificate = "d6c002e936ed4dccb928ec3481c67eb8"
    channelName = request.GET.get('channel')
    uid = random.randint(1, 230)
    expirationTimeInSeconds = 3600
    currentTimeStamp = int(time.time())
    privilegeExpiredTs = currentTimeStamp + expirationTimeInSeconds
    role = 1

    token = RtcTokenBuilder.buildTokenWithUid(appId, appCertificate, channelName, uid, role, privilegeExpiredTs)

    return JsonResponse({'token': token, 'uid': uid}, safe=False)


@csrf_exempt
def createMember(request):
    data = json.loads(request.body)
    print(data)
    member, created = RoomMember.objects.get_or_create(
        name=data['name'],
        uid=data['UID'],
        room_name=data['room_name']
    )

    return JsonResponse({'name':data['name']}, safe=False)


def getMember(request):
    uid = request.GET.get('UID')
    room_name = request.GET.get('room_name')

    member = RoomMember.objects.get(
        uid=uid,
        room_name=room_name,
    )
    name = member.name
    return JsonResponse({'name':member.name}, safe=False)

@csrf_exempt
def deleteMember(request):
    data = json.loads(request.body)
    print("Deleting a member", data)
    member = RoomMember.objects.get(
        name=data['name'],
        uid=data['UID'],
        room_name=data['room_name']
    )
    member.delete()
    return JsonResponse('Member deleted', safe=False)