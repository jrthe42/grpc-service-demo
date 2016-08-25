import time

import grpc

import jr_pb2

def get_songlist(stub):
  request = jr_pb2.SingerId(id=1)
  song_list = stub.ListSongs(request)
  for song in song_list.songs:
    print '%d, %s, %s, %s' % (time.time()*1000, song.id, song.name, song.singer.name)

def get_songs_using_stream(stub):
  request = jr_pb2.SingerId(id=1)
  songs = stub.GetSongs(request)
  for song in songs:
    print '%d, %s, %s, %s' % (time.time()*1000, song.id, song.name, song.singer.name)

def run():
  channel = grpc.insecure_channel('localhost:50051')
  stub = jr_pb2.JRServiceStub(channel)
  get_songlist(stub)
  get_songs_using_stream(stub)

if __name__ == '__main__':
  run()
