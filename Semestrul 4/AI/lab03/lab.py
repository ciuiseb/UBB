import os
import time

import matplotlib.patches as patches
import matplotlib.pyplot as plt
import numpy as np
from PIL import Image
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes
from msrest.authentication import CognitiveServicesCredentials

subscription_key = os.environ["VISION_KEY"]
endpoint = os.environ["VISION_ENDPOINT"]
computervision_client = ComputerVisionClient(endpoint, CognitiveServicesCredentials(subscription_key))

img = open("test2.jpeg", "rb")
read_response = computervision_client.read_in_stream(
    image=img,
    mode="Printed",
    raw=True
)
# print(read_response.as_dict())

operation_id = read_response.headers['Operation-Location'].split('/')[-1]
while True:
    read_result = computervision_client.get_read_result(operation_id)
    if read_result.status not in ['notStarted', 'running']:
        break
    time.sleep(1)

result = []
if read_result.status == OperationStatusCodes.succeeded:
    for text_result in read_result.analyze_result.read_results:
        for line in text_result.lines:
            print(line.text)
            result.append(line.text)

print()

text = [
    "Succes în rezolvarea",
    "tEMELOR la",
    "LABORAtoarele de",
    "Inteligență Artificială!"
]


def calculate_levenshtein_distance(str1, str2):
    return Levenshtein.distance(text, result)


def calculate_jaccardi_distance(list1, list2):
    jaccard_index = sum(
        len(set(t.split()) & set(r.split())) / len(set(t.split()) | set(r.split()))
        for t, r in zip(list1, list2)
    ) / len(text)
    return 1 - jaccard_index

def longest_common_substring(str1, str2):
    dp = [[0] * (len(str2) + 1) for _ in range(len(str1) + 1)]
    max_len = 0
    end_index = 0

    for i in range(1, len(str1) + 1):
        for j in range(1, len(str2) + 1):
            if str1[i - 1] == str2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
                if dp[i][j] > max_len:
                    max_len = dp[i][j]
                    end_index = i
            else:
                dp[i][j] = 0

    return str1[end_index - max_len: end_index]


def calculate_similarity(list1, list2):
    str1 = ''.join(list1)
    str2 = ''.join(list2)
    lcs = longest_common_substring(str1, str2)
    lcs_length = len(lcs)
    total_length = len(str1) + len(str2)

    return 1 - (2.0 * lcs_length) / total_length


print(f"Levenshtein distance pe caractere: {calculate_levenshtein_distance(text, result)}")
print(f"Similiaritate prin substrg comun: {calculate_similarity(text, result)}")
print(f"Jaccard distance pe caractere: {calculate_jaccardi_distance(text, result)}")

detected_boxes = []
detected_text = []
if read_result.status == OperationStatusCodes.succeeded:
    for text_result in read_result.analyze_result.read_results:
        for line in text_result.lines:
            detected_boxes.append({
                'text': line.text,
                'box': line.bounding_box
            })

ground_truth_boxes = [
    {'text': "Succes în rezolvarea", 'box': [120, 370, 720, 370, 720, 450, 120, 450]},
    {'text': "tEMELOR la", 'box': [120, 630, 500, 630, 500, 710, 120, 710]},
    {'text': "LABORAtoarele de", 'box': [120, 890, 600, 890, 600, 970, 120, 970]},
    {'text': "Inteligență Artificială!", 'box': [120, 1150, 120, 1280, 650, 1280, 650, 1150]}
]


def plot_boxes(image_path, boxes, ground_boxes, title):
    img = Image.open(image_path)
    fig, ax = plt.subplots(1)
    ax.imshow(np.array(img))

    colors = ['r', 'g', 'b', 'y']
    for i, box_data in enumerate(boxes):
        box = box_data['box']
        color = colors[i % len(colors)]

        polygon = patches.Polygon([
            (box[0], box[1]), (box[2], box[3]),
            (box[4], box[5]), (box[6], box[7])
        ], linewidth=2, edgecolor='g', facecolor='none', linestyle='-')

        ax.add_patch(polygon)

    for i, box_data in enumerate(ground_boxes):
        box = box_data['box']
        polygon = patches.Polygon([
            (box[0], box[1]), (box[2], box[3]),
            (box[4], box[5]), (box[6], box[7])
        ], linewidth=2, edgecolor='r', facecolor='none', linestyle='--')

        ax.add_patch(polygon)
    plt.title(title)
    plt.show()


plot_boxes("test2.jpeg", detected_boxes, ground_truth_boxes, "Detected Boxes")