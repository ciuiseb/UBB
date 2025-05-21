import os

from azure.ai.textanalytics import TextAnalyticsClient
from azure.core.credentials import AzureKeyCredential

endpoint = os.environ["VISION_ENDPOINT"]
key = os.environ["VISION_KEY"]

client = TextAnalyticsClient(endpoint=endpoint, credential=AzureKeyCredential(key))

documents = [
    "Fermín, I need your help.This is serious.",
    "Daniel, my boy, you can count on me for anything. If necessary, I would give my last drop of blood for you.Well, perhaps not the last, because then I'd be dead and couldn't help you anymore, but you understand what I mean.",

    "I know, Fermín. And I thank you for it.",
    "No need for thanks among friends. Friends are God's apology for relatives."
]

result = client.analyze_sentiment(documents, show_opinion_mining=True)
docs = [doc for doc in result if not doc.is_error]

for idx, doc in enumerate(docs):
    print(f"Document text: {documents[idx]}")
    print(f"Overall sentiment: {doc.sentiment}")
    print()
