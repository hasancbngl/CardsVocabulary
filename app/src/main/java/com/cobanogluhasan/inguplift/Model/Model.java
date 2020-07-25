package com.cobanogluhasan.inguplift.Model;

    public class Model {

        public String title,image,word;

        public Model(String title, String description,String word ) {

            this.title = title;
            this.image = description;
            this.word = word;

        }

        public String getTitle() {

            return title;
        }

        public void setTitle(String title) {

            this.title = title;

        }

        public String getImage() {
            return image;

        }

        public void setDescription(String image) {

            this.image = image;
        }


        public String getWord() {

            return word;
        }

        public void setWord(String word) {
            this.word = word;

        }




    }


