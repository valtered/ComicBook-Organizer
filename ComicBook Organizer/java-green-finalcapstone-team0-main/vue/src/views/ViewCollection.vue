<template>
  <div class="collection-page">
    <h1>{{ collection.name }}</h1>

    <!-- Collection Stats -->
    <div class="collection-stats">
      <h2>Collection Statistics</h2>
      <!-- Description under the title -->
      <div class="description-box">
        <p>{{ collection.description }}</p>
      </div>

      <div class="stats-wrapper">
        <div class="stat-box">
          <h3>Total Comics</h3>
          <p class="stat-value">{{ comics.length }}</p>
        </div>
        <div class="stat-box">
          <h3>Liked Comics</h3>
          <p class="stat-value">{{ likedComicsCount }}</p>
        </div>
        <div class="stat-box">
          <h3>Comics per Author</h3>
          <ul class="author-stats">
            <li v-for="(count, author) in authorStats" :key="author">
              <span>{{ author }}: </span><span>{{ count }}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Search and Filter -->
    <div class="search-filter">
      <input type="text" v-model="searchQuery" placeholder="Search comics..." @input="filterComics" />
      <div class="filters">
        <select v-model="selectedTitle" @change="filterComics">
          <option value="">Filter by Title</option>
          <option v-for="comic in titleOptions" :key="comic" :value="comic">{{ comic }}</option>
        </select>
        <select v-model="selectedLiked" @change="filterComics">
          <option value="">Filter by Liked</option>
          <option value="true">Liked</option>
          <option value="false">Not Liked</option>
        </select>
        <select v-model="selectedAuthor" @change="filterComics">
          <option value="">Filter by Author</option>
          <option v-for="author in authors" :key="author" :value="author">{{ author }}</option>
        </select>
        <select v-model="sortOption" @change="sortComics">
          <option value="title">Sort by Title</option>
          <option value="issueNumber">Sort by Issue Number</option>
          <option value="dateAdded">Sort by Date Added</option>
        </select>
      </div>
    </div>

    <!-- Comic Count -->
    <p v-if="displayedComics.length" class="comic-count">
      Showing {{ displayedComics.length }} out of {{ comics.length }} comics
    </p>
    <!-- Add Comic Button -->
    <router-link to="/add-comic" class="add-comic-button">Add Comic</router-link>
    <!-- No Comics -->
    <div v-if="!displayedComics.length" class="empty-collection">
      <p>No comics in this collection yet.</p>
    </div>
    <!-- Comics Display -->
    <div v-else class="comics-grid">
      <div v-for="(comic, index) in displayedComics" :key="comic.comicId" class="comic-card">
        <div class="comic-content">
          <div class="comic-header">
            <h3>{{ comic.title }}</h3>
            <span class="issue-badge">#{{ comic.issueNumber }}</span>
          </div>
          <img v-if="comic.coverImage" :src="comic.coverImage" alt="Comic Cover" class="comic-cover-image" />
          <div class="comic-details">



            <!--<div class="detail-row">
              <span class="label">Publisher:</span>
              <span class="value">{{ comic.publisher }}</span>
            </div> 
          -->



            <div v-if="comic.author" class="detail-row">
              <span class="label">Author:</span>
              <span class="value">{{ comic.author }}</span>
            </div>
            <div v-if="comic.character" class="detail-row">
              <span class="label">Character:</span>
              <span class="value">{{ comic.character }}</span>
            </div>
          </div>
          <!-- Like/Bookmark Button -->
          <button class="like-button" @click="toggleLike(comic)">
            <span v-if="comic.liked">❤️</span>
            <span v-else>♡</span>
            Like
          </button>
        </div>
      </div>
      <!-- No more comics allowed -->
      <div v-if="comics.length >= 100" class="no-more-comics">
        <p>No more comics allowed.</p>
      </div>
    </div>
  </div>
</template>









<script>
import axios from 'axios';
export default {
  name: 'ViewCollection',
  data() {
    return {
      collection: {}, // Collection details
      comics: [], // Array to store comics
      displayedComics: [], // Array to store filtered comics for display
      searchQuery: '', // Search query input
      selectedTitle: '', // Selected Title for filter
      selectedLiked: '', // Selected Liked for filter
      selectedAuthor: '', // Selected Author for filter
      authors: [], // List of authors for filtering
      titleOptions: [], // List of unique titles for filtering
      sortOption: 'title', // Sorting criteria (default is by title)
      likedComicsCount: 0, // To store the number of liked comics
      authorStats: {} // To store comic count per author
    };
  },
  created() {
    this.fetchCollectionDetails();
  },
  methods: {
    fetchCollectionDetails() {
      const collectionId = this.$route.params.id;
      axios.get(`http://localhost:9000/collections/${collectionId}`)
        .then(response => {
          this.collection = response.data;
          return axios.get(`http://localhost:9000/collections/${collectionId}/comics`);
        })
        .then(response => {
          this.comics = response.data;
        })
        .catch(error => {
          console.error('Error fetching collection details or comics:', error);
        });
      // Fetch collection details
      axios.get(`http://localhost:9000/collections/${collectionId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then(response => {
        this.collection = response.data; // Store collection details
        this.fetchComics(collectionId); // Fetch comics for this collection
      })
      .catch(error => {
        console.error('Error fetching collection details:', error);
      });
    },
    fetchComics(collectionId) {
      axios.get(`http://localhost:9000/collections/${collectionId}/comics`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then(response => {
        this.comics = response.data; // Store all comics
        this.displayedComics = this.comics; // Initially display all comics
        this.extractTitleOptions(); // Populate title filter options
        this.extractAuthorOptions(); // Populate author filter options
        this.updateCollectionStats(); // Update collection statistics
      })
      .catch(error => {
        console.error('Error fetching comics:', error);
      });
    },
    extractTitleOptions() {
      this.titleOptions = [...new Set(this.comics.map(comic => comic.title))];
    },
    extractAuthorOptions() {
      this.authors = [...new Set(this.comics.map(comic => comic.author).filter(Boolean))];
    },
    updateCollectionStats() {
      // Update the liked comics count
      this.likedComicsCount = this.comics.filter(comic => comic.liked).length;
      // Update the comic count per author
      this.authorStats = this.comics.reduce((stats, comic) => {
        if (comic.author) {
          stats[comic.author] = (stats[comic.author] || 0) + 1;
        }
        return stats;
      }, {});
    },
    filterComics() {
      let filteredComics = this.comics;
      // Apply Search Query
      if (this.searchQuery) {
        filteredComics = filteredComics.filter(comic =>
          comic.title.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }
      // Apply Title Filter
      if (this.selectedTitle) {
        filteredComics = filteredComics.filter(comic => comic.title === this.selectedTitle);
      }
      // Apply Liked Filter
      if (this.selectedLiked === 'true') {
        filteredComics = filteredComics.filter(comic => comic.liked);
      } else if (this.selectedLiked === 'false') {
        filteredComics = filteredComics.filter(comic => !comic.liked);
      }
      // Apply Author Filter
      if (this.selectedAuthor) {
        filteredComics = filteredComics.filter(comic => comic.author === this.selectedAuthor);
      }
      this.displayedComics = filteredComics;
      this.updateCollectionStats(); // Update stats after filtering
    },
    toggleLike(comic) {
      comic.liked = !comic.liked;
      // Update the comic's like status in the database or on the server
      axios.put(`http://localhost:9000/comics/${comic.comicId}`, comic, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })
      .then(response => {
        console.log('Like status updated');
      })
      .catch(error => {
        console.error('Error updating like status:', error);
      });
    },
    sortComics() {
      let sortedComics = [...this.displayedComics];
      switch (this.sortOption) {
        case 'title':
          sortedComics.sort((a, b) => a.title.localeCompare(b.title));
          break;
        case 'issueNumber':
          sortedComics.sort((a, b) => a.issueNumber - b.issueNumber);
          break;
        case 'dateAdded':
          sortedComics.sort((a, b) => new Date(b.dateAdded) - new Date(a.dateAdded));
          break;
      }
      this.displayedComics = sortedComics;
    }
  }
};
</script>




Message nlr24-cap-comicbook



<style scoped>
 .collection-page {
  margin: 0 auto;
  padding: 20px;
  background-color: #dddddd;
  min-height: 100vh;
  background-image: url('@/assets/superhero-symbols.jpg');
  background-size: cover;
  background-position: center;
  color: #2c3e50;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
  font-size: 6em; /* Increased font size */
  font-weight: bold; /* Bold text */
  text-transform: uppercase;
  color: black;
}

.collection-stats {
  background: rgba(255, 255, 255, 0.85);
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.collection-stats h2 {
  font-size: 2em;
  margin-bottom: 20px;
}

.description-box {
  background-color: #f8f9fa; /* Light background for visibility */
  padding: 15px;
  margin-bottom: 20px; /* Adds space between description and stats */
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.description-box p {
  font-size: 1.2em;
  color: black;
  margin: 0;
}

.stats-wrapper {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.stat-box {
  background: #3498db;              /* Blue background for better visibility */
  color: white;                     /* White text */
  padding: 30px;                    /* Increased padding for better spacing */
  border-radius: 12px;              /* Rounded corners for a softer look */
  flex: 1;                          /* Flexible box for equal distribution */
  min-width: 220px;                 /* Minimum width to ensure consistency */
  text-align: center;               /* Center align the text */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Slight shadow for depth */
  transition: transform 0.3s ease;  /* Smooth hover effect */
}

.stat-box:hover {
  transform: translateY(-5px);      /* Elevate the box on hover */
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3); /* Enhance shadow on hover */
}

.stat-value {
  font-size: 3em;                   /* Make the value much larger */
  font-weight: bold;                /* Bold the value */
  margin-top: 10px;                 /* Add space between the label and value */
}

.stat-box h3 {
  font-size: 1.5em;                 /* Slightly bigger heading */
  font-weight: 600;                 /* Make the heading semi-bold */
  margin-bottom: 15px;              /* Space between the heading and value */
}

.comics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 20px 0;
  perspective: 1000px; 
}

.comic-card {
  background: rgba(255, 255, 255, 0.85);
  border-radius: 12px;
  border: 2px solid #ccc;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  height: 100%;
   transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.comic-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 14px rgba(0, 0, 0, 0.2);
}

.comic-content {
  padding: 20px;
}

.comic-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.comic-header h3 {
  text-decoration: bold; /* This will underline the title */
  font-size: 2.5em; /* Increase the font size for the title */
  font-weight: bold; /* Make the title bold */
  margin: 0; 
}

.issue-badge {
  background: #e74c3c;
  color: white;
  padding: 4px 10px;
  border-radius: 20px;
}

.comic-cover-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
}

.detail-row {
  display: flex;
  justify-content: flex-start;  /* Align items to the left */
  align-items: center;         /* Vertically center the items */
  gap: 5px;
}



.like-button {
  padding: 8px 12px;               /* Padding for better size */
  background: #3498db;               /* Blue background */
  color: white;                      /* White text */
  border-radius: 0px;               /* Rounded corners */
  font-size: 1em;                  /* Slightly larger text */
  font-weight: bold;                 /* Bold text */
  border: 2px solid transparent;     /* Transparent border by default */
  cursor: pointer;                  /* Pointer cursor on hover */
  transition: background-color 0.3s ease, color 0.3s ease, border 0.3s ease; /* Smooth transition */
  margin-top: 10px;
}

.like-button:hover {
  background-color: white;           /* White background on hover */
  color: #e74c3c;                    /* Red text on hover */
  border: 2px solid #2c3e50;         /* Black border on hover */
}

.like-button:focus {
  outline: none;                     /* Remove focus outline */
  box-shadow: 0 0 10px rgba(52, 152, 219, 0.7); /* Optional: add a glowing effect */
}

.search-filter {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 30px;
  padding: 0 20px;
}

.search-filter input {
  padding: 12px;
  font-size: 1.1em;
  border-radius: 8px;
  border: 1px solid #3498db;
  width: 100%;
}

.search-filter input:focus {
  border-color: #2980b9;
  outline: none;
}

.filters {
  display: flex;
  gap: 15px;
}

.filters select, .filters input {
  padding: 10px;
  font-size: 1.1em;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.add-comic-button {
  display: inline-block;
  margin-top: 20px;
  padding: 12px 24px;
  background: #3498db;
  color: white;
  text-decoration: none;
  border-radius: 25px;
  font-weight: 600;
}

.add-comic-button:hover {
  background: #2980b9;
}

.comic-count {
  font-size: 1.3em; /* Increase the font size */
  color: white;     /* Set text color to white */
  text-align: left;
  margin-top: 20px;
  font-weight: bold; /* Optional: Makes the text bold for emphasis */
}





.author-stats li:last-child {
  border-bottom: none;              /* Remove the border on the last list item */
}

.author-stats .label {
  font-weight: bold;                /* Make the author name bold */
  color: #2c3e50;                   /* Darker color for the label */
}

.author-stats .count {
  font-weight: bold;                /* Make the count bold */
  color: #3498db;                   /* Use a different color for the count to make it stand out */
  font-size: 1.4em;                 /* Increase the font size of the count */
}


</style>
